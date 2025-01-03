"use client";

import { useEffect, useState } from "react";
import RoomCard from "../../components/RoomCard"; // Importamos RoomCard

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
    const [informationList, setInformationList] = useState<Information[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function fetchInformation() {
            try {
                const response = await fetch("/api/information/get-all/");
                if (response.ok) {
                    const data: { data: Information[] } = await response.json();
                    setInformationList(data.data);
                } else {
                    const errorData = await response.json();
                    setError(errorData.error);
                }
            } catch (err) {
                setError("Error al obtener la información");
            }
        }

        fetchInformation();
    }, []);

    if (error) {
        return <div className="text-red-500 text-center mt-4">Error: {error}</div>;
    }

    return (
        <div className="bg-gradient-to-b from-gray-200 to-white dark:from-black dark:to-gray-800 text-gray-800 dark:text-gray-200 min-h-screen p-6">
            <h1 className="text-3xl font-bold text-center mb-6">Lista de Habitaciones</h1>
            <ul className="grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
                {informationList.map((info) => (
                    <RoomCard key={info.id} info={info} />
                ))}
            </ul>
        </div>
    );
}
