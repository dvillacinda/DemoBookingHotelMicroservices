import axios from "axios";
import { NextResponse } from "next/server";
import { getAccessToken } from "../../../../utils/sessionTokenAccessor";
import { getServerSession } from "next-auth";
import { authOptions } from "../../auth/[...nextauth]/route";
import type { NextRequest } from "next/server";

export async function POST(req: NextRequest): Promise<Response> {
    try {
        axios.defaults.withCredentials = true;

        const session = await getServerSession(authOptions);

        if (!session) {
            return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
        }

        if (!process.env.DEMO_BACKEND_URL) {
            console.error("Missing environment variable DEMO_BACKEND_URL");
            return NextResponse.json({ error: "Server configuration error" }, { status: 500 });
        }
        const { searchParams } = new URL(req.url);
        const roomId = searchParams.get("roomId");
        const reservationIdHash = searchParams.get("reservationIdHash");
        const roomPrice = searchParams.get("roomPrice");
        if (!roomId) {
            return NextResponse.json({ error: "Missing roomId parameters" }, { status: 400 });
        }
        
        const url = `${process.env.DEMO_BACKEND_URL}/api/reservation/select-room-by-roomId`;

        try {
            const accessToken = await getAccessToken();
            const fullUrl = `${url}?roomId=${roomId}&reservationIdHash=${reservationIdHash}&roomPrice=${roomPrice}`;
            const response = await axios.post(fullUrl, null, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                    "Content-Type": "aplication/json"
                },
                withCredentials: true,
            }); 
            return NextResponse.json({ status: response.status });
        } catch (error: any) {
            const status = error.response?.status || 500;
            const message = error.response?.data || "Error fetching data";
            return NextResponse.json({ error: message }, { status });
        }
    } catch (error) {
        console.error("Error in GET handler:", error);
        return NextResponse.json({ error: "Internal server error" }, { status: 500 });
    }
}

