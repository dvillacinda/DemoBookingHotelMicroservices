'use client';

import { useEffect, useState } from "react";

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
                const response = await fetch("/api/information/get-all");
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
                    <li key={info.id} className="border rounded-lg shadow-lg p-4 bg-white dark:bg-gray-900">
                        <h2 className="text-xl font-semibold text-blue-600 mb-2">Habitación {info.roomNumber}</h2>
                        <p className="text-lg mb-2"><span className="font-bold">Capacidad:</span> {info.capacity} personas</p>
                        <p className="text-lg mb-2"><span className="font-bold">Descripción:</span> {info.description}</p>
                        <p className="text-lg mb-2"><span className="font-bold">Precio:</span> ${info.price}</p>
                        <p className="text-lg mb-4"><span className="font-bold">Servicios incluidos:</span> {info.servicesInclude}</p>

                        <div>
                            {info.photos.length > 0 ? (
                                <div className="flex flex-wrap justify-center gap-2 mt-2" >

                                    <img 
                                        key={info.photos[0].id}
                                        src={info.photos[0].url}
                                        alt={`Foto de la habitación ${info.roomNumber}`}
                                        className="w-full h-full object-cover rounded-lg border"
                                    />

                                </div>
                            ) : (
                                <p className="text-gray-500">No hay fotos disponibles</p>
                            )}
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}
