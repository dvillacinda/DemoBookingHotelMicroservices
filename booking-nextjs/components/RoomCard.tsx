"use client";
import { useState } from "react";

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

interface RoomCardProps {
    info: Information;
    onReserve?: (roomId: number) => void; // Optional property for the reservation
    isAvailable?: boolean; // Optional property to indicate if the room is available
}

const RoomCard: React.FC<RoomCardProps> = ({ info, onReserve, isAvailable }) => {
    const [selectedPhoto, setSelectedPhoto] = useState<string | null>(null);

    const handleImageClick = (url: string) => {
        setSelectedPhoto(url);
    };

    const closeModal = () => {
        setSelectedPhoto(null);
    };

    return (
        <li className="border rounded-lg shadow-lg p-4 bg-white dark:bg-gray-900">
            <h2 className="text-xl font-semibold text-blue-600 mb-2">Habitación {info.roomNumber}</h2>
            <p className="text-lg mb-2"><span className="font-bold">Capacidad:</span> {info.capacity} personas</p>
            <p className="text-lg mb-2"><span className="font-bold">Descripción:</span> {info.description}</p>
            <p className="text-lg mb-2"><span className="font-bold">Precio:</span> ${info.price}</p>
            <p className="text-lg mb-4"><span className="font-bold">Servicios incluidos:</span> {info.servicesInclude}</p>

            <div>
                {info.photos.length > 0 ? (
                    <div className="flex flex-wrap justify-center gap-2 mt-2">
                        {info.photos.map((photo) => (
                            <div key={photo.id} className="relative">
                                <img
                                    src={photo.url}
                                    alt={`Foto de la habitación ${info.roomNumber}`}
                                    className="w-20 h-20 object-cover rounded-lg border cursor-pointer transition-transform duration-300 ease-in-out transform hover:scale-125"
                                    onClick={() => handleImageClick(photo.url)}
                                />
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="text-gray-500">No hay fotos disponibles</p>
                )}
            </div>

            {/* Botón de reserva solo si la habitación está disponible */}
            {onReserve && isAvailable !== undefined && (
                <button
                    onClick={() => onReserve(info.id)}
                    className={`mt-4 w-full py-2 px-4 rounded-lg transition duration-200 ${
                        isAvailable ? "bg-blue-600 text-white hover:bg-blue-700" : "bg-gray-400 text-white cursor-not-allowed"
                    }`}
                    disabled={!isAvailable}
                >
                    {isAvailable ? "Reservar" : "No disponible"}
                </button>
            )}

            {/* Modal para mostrar la imagen ampliada */}
            {selectedPhoto && (
                <div className="fixed inset-0 bg-gray-800 bg-opacity-75 flex justify-center items-center z-50">
                    <div className="relative">
                        <img style={{ maxHeight: "60vh" , maxWidth: "60vw" }}
                            src={selectedPhoto}
                            alt="Imagen ampliada"
                            className="object-contain rounded-lg"
                            width={800}
                            height={600}
                        />
                        <button
                            onClick={closeModal}
                            className="absolute top-2 right-2 bg-gray-700 text-white p-2 rounded-full"
                        >
                            X
                        </button>
                    </div>
                </div>
            )}
        </li>
    );
};

export default RoomCard;
