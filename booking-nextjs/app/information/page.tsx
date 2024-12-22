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
        return <div>Error: {error}</div>;
    }

    return (
        <div>
            <h1>Lista de Habitaciones</h1>
            <ul>
                {informationList.map((info) => (
                    <li key={info.id}>
                        <h2>Habitación {info.roomNumber}</h2>
                        <p><strong>Capacidad:</strong> {info.capacity} personas</p>
                        <p><strong>Descripción:</strong> {info.description}</p>
                        <p><strong>Precio:</strong> ${info.price}</p>
                        <p><strong>Servicios incluidos:</strong> {info.servicesInclude}</p>

                        {/* Mostrar fotos si existen */}
                        <div>
                            <strong>Fotos:</strong>
                            {info.photos.length > 0 ? (
                                <div>
                                    {info.photos.map((photo) => (
                                        <img key={photo.id} src={photo.url} alt={`Foto de la habitación ${info.roomNumber}`} style={{ width: '100px', height: '100px', margin: '5px' }} />
                                    ))}
                                </div>
                            ) : (
                                <p>No hay fotos disponibles</p>
                            )}
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}
