import axios from "axios";
import { NextResponse } from "next/server";
import { getAccessToken } from "../../../../utils/sessionTokenAccessor";
import { getServerSession } from "next-auth";
import { authOptions } from "../../auth/[...nextauth]/route";
import type { NextRequest } from "next/server";

export async function POST(req: NextRequest): Promise<Response> {
    try {
        const session = await getServerSession(authOptions);

        if (!session) {
            return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
        }

        if (!process.env.DEMO_BACKEND_URL) {
            console.error("Missing environment variable DEMO_BACKEND_URL");
            return NextResponse.json({ error: "Server configuration error" }, { status: 500 });
        }
        const requestBody = await req.json(); 
        const url = `${process.env.DEMO_BACKEND_URL}/api/reservation/init-reservation`;

        try {
            const accessToken = await getAccessToken();
            const response = await axios.post(url, requestBody,{
                headers: {
                    Authorization: `Bearer ${accessToken}`,
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

