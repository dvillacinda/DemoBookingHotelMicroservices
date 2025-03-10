"use client";

import { useEffect, useState } from "react";
import DateSelector from "@/components/DateSelector";
import dayjs, { Dayjs } from "dayjs";

import RoomCard from "@/components/RoomCard";
import ConfirmView from "@/components/ConfirmView";
interface Photo {
    id: number;
    url: string;
}

interface Information {
    roomId: number,
    capacity: number;
    description: string;
    price: number;
    servicesInclude: string;
    photos?: Photo[];
}

export default function InformationList() {
    const [informationList, setInformationList] = useState<Information[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [checkInDate, setCheckInDate] = useState<string | null>(null);
    const [checkOutDate, setCheckOutDate] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [isAuth, setIsAuth] = useState(false);
    const [isConfirmViewOpen, setIsConfirmViewOpen] = useState(false);
    const [isSuccessViewOpen, setIsSuccessViewOpen] = useState(false); // Nuevo estado
    const [selectedRoom, setSelectedRoom] = useState<number | null>(null);
    const [roomHashId, setRoomHashId] = useState<string | null>(null);
    const [roomPrice, setRoomPrice] = useState<number | null>(null);

    // reset values
    const resetAllValues = () => {
        setCheckInDate(null);
        setCheckOutDate(null);
        setInformationList([]); // Limpiar la lista de habitaciones
    };


    // Date format 
    const formatDate = (date: Date): string => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Meses van de 0-11, por eso sumamos 1
        const day = String(date.getDate()).padStart(2, '0');

        return `${year}-${month}-${day}`;
    };

    // Function to handle check-in and check-out date changes
    const handleDateChange = (
        date: Dayjs | null,
        dateString: string | string[],
        type: "checkIn" | "checkOut"
    ) => {
        const formattedDateString = Array.isArray(dateString)
            ? dateString[0]
            : dateString;

        if (type === "checkIn") {
            setCheckInDate(formattedDateString);

            // Si la nueva fecha de Check-In es posterior o igual a la de Check-Out, reinicia Check-Out
            if (
                checkOutDate &&
                new Date(formattedDateString) >= new Date(checkOutDate as string)
            ) {
                setCheckOutDate(null);
            }
        } else if (type === "checkOut") {
            setCheckOutDate(formattedDateString);

            // Si la nueva fecha de Check-Out es anterior o igual a la de Check-In, reinicia Check-In
            if (
                checkInDate &&
                new Date(formattedDateString) <= new Date(checkInDate as string)
            ) {
                setCheckInDate(null);
            }
        }
    };

    // Authenticate user on component load
    useEffect(() => {
        async function auth() {
            try {
                const response = await fetch("/api/auth/validate-session");
                if (response.ok) {
                    setIsAuth(true);
                } else {
                    const errorData = await response.json();
                    setError(errorData.error);
                    setIsAuth(false);
                }
            } catch {
                setError("Unexpected error while validating session.");
            }
        }
        auth();
    }, []);

    // Fetch available room data based on selected dates
    const fetchInformation = async () => {
        setIsLoading(true);
        setError(null);
        setInformationList([]);

        try {
            console.log("Fetching data with dates:", { checkInDate, checkOutDate });

            const response = await fetch(
                `/api/reservation/get-available-rooms?startString=${checkInDate}&endString=${checkOutDate}`
            );

            if (response.ok) {
                const data: { data: { rooms: Information[] } } = await response.json();
                console.log("API Response:", data);
                setInformationList(data.data.rooms || []);
            } else {
                const errorData = await response.json();
                console.error("API Error:", errorData);
                setError(errorData.error || "Unexpected error while fetching data.");
            }
        } catch (error) {
            console.error("Fetch error:", error);
            setError("Error while fetching data.");
        } finally {
            setIsLoading(false); // Reset loading state
        }
    };

    // fetch init reservaation 
    const fetchInitReservation = async () => {
        setError(null);
        try {
            setError(null);

            console.log("Init reservation");
            const response = await fetch(`/api/reservation/init-reservation`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    user_id: 1,
                    start_date: checkInDate,
                    end_date: checkOutDate,
                    status: false,
                    reservation_date: formatDate(new Date),
                    payment_status: false
                })

            });
            if (response.ok) {
                const data: { data: { reservationIdHash: string } } = await response.json();
                console.log("hash code: ", data.data.reservationIdHash);
                setRoomHashId(data.data.reservationIdHash || null);
            } else {
                setError("Error: Unexpected error with init reservation");
            }
        } catch (error) {
            console.error("Fetch error:", error);
            setError("Error while init reservation.");
        }
    };

    const fetchCompleteReservation = async () => {
        try {
            setError(null);
            console.log("Init reservation");
            const response = await fetch(
                `/api/reservation/complete-reservation?roomId=${selectedRoom}&reservationIdHash=${roomHashId}&roomPrice=${roomPrice}`
                , {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    }

                });
            if (response.ok) {

                return;
            } else {
                setError("Error: Unexpected error with complete reservation");
            }
        } catch (error) {
            console.error("Fetch error:", error);
            setError("Error while init reservation.");
        }
    };

    // Confirm the selected dates and fetch room availability
    const handleConfirmDates = () => {
        if (!checkInDate || !checkOutDate) {
            setError("Please select both Check-In and Check-Out dates.");
            return;
        }

        if (new Date(checkInDate) >= new Date(checkOutDate)) {
            setError("Check-In date must be earlier than Check-Out date.");
            return;
        }
        fetchInitReservation();
        fetchInformation();
    };

    // Do Reservation 
    const handleRoomReserve = (roomId: number, index?: number) => {
        if (index !== undefined) {
            setSelectedRoom(roomId);

            setRoomPrice(informationList[index].price);

            setIsConfirmViewOpen(true);
        } else {
            setError("Error: Index room undefined!");
        }
    };

    const handleConfirmReservation = () => {
        if (selectedRoom !== null) {
            fetchCompleteReservation();
            if (!error) {
                // Esto incluye casos donde error es null, undefined, false, 0, "", etc.
                console.log(`Room ${selectedRoom} reserved!`);
                setIsSuccessViewOpen(true);
            } else {
                console.log("Error with completing your reservation, error value:", error);
                setError("Error with completing your reservation, please try later");
            }

        }
        resetAllValues();
        setIsConfirmViewOpen(false); // Cerramos el ConfirmView
    };

    const handleCancelReservation = () => {
        setIsConfirmViewOpen(false); // Cerramos el ConfirmView sin hacer nada
    };

    const handleCloseSuccessView = () => {
        setIsSuccessViewOpen(false); // Cierra la ventana emergente de éxito
    };


    // React to changes in informationList and isLoading
    useEffect(() => {
        if (!isLoading && informationList.length === 0 && !error) {
            setError("No available rooms for the selected dates.");
        }
    }, [informationList, isLoading]);

    // Render the main UI
    const renderView = () => (
        <div className="container mx-auto p-5">
            <h1 className="text-3xl font-bold text-blue-600 mb-5">Room Availability</h1>

            {/* Date selectors */}
            <DateSelector
                label="Select Check-In Date"
                value={checkInDate ? dayjs(checkInDate) : null} // Convertir el string a Dayjs o null
                onChange={(date, dateString) => handleDateChange(date, dateString, "checkIn")}
            />
            <DateSelector
                label="Select Check-Out Date"
                value={checkOutDate ? dayjs(checkOutDate) : null} // Convertir el string a Dayjs o null
                onChange={(date, dateString) => handleDateChange(date, dateString, "checkOut")}
            />


            {/* Confirm dates button */}
            <button
                onClick={handleConfirmDates}
                className={`bg-blue-500 text-white px-4 py-2 rounded-md ${isLoading ? "opacity-50" : ""}`}
                disabled={isLoading}
            >
                {isLoading ? "Finding rooms..." : "Confirm Dates"}
            </button>

            {/* Error messages */}
            {error && <div className="text-red-500 text-center mt-4">{error}</div>}

            {/* Display room information */}
            { }
            {!isLoading && informationList.length > 0 && !error && (
                <ul className="grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-3" style={{ marginTop: '1%' }}>
                    {informationList.map((room, index) => (
                        <RoomCard
                            key={room.roomId} // Pass a unique key
                            info={room} // Pass room information to RoomCard
                            isAvailable={true} // You can toggle availability dynamically if needed
                            onReserve={(roomId) => handleRoomReserve(roomId, index)} // Example callback for reservation
                        />
                    ))}
                </ul>
            )}

            {/* Message for no available rooms */}
            {!isLoading && informationList.length === 0 && !error && (
                <p className="text-center text-gray-500 mt-6">
                    No rooms available for the selected dates.
                </p>
            )}
            {/* Confirm View Modal */}
            <ConfirmView
                isOpen={isConfirmViewOpen}
                onClose={handleCancelReservation}
                onConfirm={handleConfirmReservation}
                message="Are you sure you want to reserve this room?"
            />

            <ConfirmView
                isOpen={isSuccessViewOpen}
                onClose={handleCloseSuccessView}
                onConfirm={handleCloseSuccessView}
                message={`Room ${selectedRoom} reserved successfully!`}
            />
        </div>
    );

    // Render the unauthorized view
    const renderUnauthorizedView = () => (
        <div className="flex flex-col justify-center items-center min-h-screen bg-gray-100">
            <div className="text-center p-6 bg-white shadow-md rounded-lg max-w-lg w-full">
                <h1 className="text-4xl font-bold text-red-600 mb-4">401 - Unauthorized</h1>
                <p className="text-xl text-gray-700 mb-6">
                    You do not have permission to access this page.
                </p>
            </div>
        </div>
    );

    // Render based on authentication status
    return isAuth ? renderView() : renderUnauthorizedView();
}
