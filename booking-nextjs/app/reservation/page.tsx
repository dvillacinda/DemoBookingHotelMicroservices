"use client";

import { useEffect, useState } from "react";
import DateSelector from "@/components/DateSelector";
import { Dayjs } from "dayjs";
import RoomCard from "@/components/RoomCard";
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

    // Function to handle check-in and check-out date changes
    const handleDateChange = (date: Dayjs | null, dateString: string | string[], type: "checkIn" | "checkOut") => {
        if (type === "checkIn") setCheckInDate(dateString as string);
        else setCheckOutDate(dateString as string);
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
        setIsLoading(true); // Indicate data is being loaded
        setError(null); // Clear previous errors
        setInformationList([]); // Clear previous room data

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

        fetchInformation();
    };

    // Do Reservation 
    const handleRoomReserve = (roomId: number, index?: number) => {
        if (index !== undefined) {
            alert(`Room ${roomId} reserved at position ${index}!`);
        } else {
            alert(`Room ${roomId} reserved!`);
        }
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
                onChange={(date, dateString) => handleDateChange(date, dateString, "checkIn")}
            />
            <DateSelector
                label="Select Check-Out Date"
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
            {!isLoading && informationList.length > 0 && (
                <ul className="grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-3"style={{marginTop:'1%'}}>
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
