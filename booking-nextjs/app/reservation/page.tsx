"use client";

import { useEffect, useState } from "react";
import RoomCard from "@/components/RoomCard"; 
import DateSelector from "@/components/DateSelector";
import { Dayjs } from "dayjs";

interface Photo {
    id: number;
    url: string;
}

interface Information {
    id: number;
    roomNumber: number;
    capacity: number;
    description: string;
    price: number;
    servicesInclude: string;
    photos: Photo[];
}

export default function InformationList() {
    const [selectedDate, setSelectedDate] = useState<string | null>(null);
    
        const handleDateChange = (date: Dayjs | null, dateString: string | string[]) => {
            setSelectedDate(dateString as string);
        };
    
        const handleReserve = (roomId: number) => {
            alert(`Reserva confirmada para la habitación ${roomId}`);
        };
    
        const [roomsList, setRoomsList] = useState<Information[]>([]);
    
    
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function fetchRooms() {
            try {
                const response = await fetch("/api/reservation/get-av/");
                if (response.ok) {
                    const data: { data: Information[] } = await response.json();
                    setRoomsList(data.data);
                }
            } catch (err) {
                console.error("Error fetching rooms:", err);
            }
        }

        fetchRooms();
    }, []);

    if (error) {
        return <div className="text-red-500 text-center mt-4">Error: {error}</div>;
    }

    return (
        <div className="container mx-auto p-5">
            <h1 className="text-3xl font-bold text-blue-600 mb-5">Disponibilidad de Habitaciones</h1>

            {/* Componente de selección de fecha */}
            <DateSelector label="Selecciona una fecha" onChange={handleDateChange} />

            {selectedDate && (
                <p className="mb-5 text-gray-600">Fecha seleccionada: {selectedDate}</p>
            )}

            <ul className="space-y-6">
                {roomsList.map((room) => (
                    <RoomCard
                        key={room.id}
                        info={room}
                        onReserve={handleReserve}
                        isAvailable={true}
                    />
                ))}
            </ul>
        </div>
    );
}
