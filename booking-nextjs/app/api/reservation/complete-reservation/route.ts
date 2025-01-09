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
        const { searchParams } = new URL(req.url);
        const position = searchParams.get("position");
        if (!position) {
            return NextResponse.json({ error: "Missing position parameters" }, { status: 400 });
        }
        
        const url = `${process.env.DEMO_BACKEND_URL}/api/reservation/select-room-by-position`;

        try {
            const accessToken = await getAccessToken();
            const fullUrl = `${url}?position=${position}`;
            const response = await axios.post(fullUrl, null, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
                withCredentials: true,
            }); 
            console.log("response: ",response);
            return NextResponse.json({ status: response.status });
        } catch (error: any) {
            console.log("error: ",error);
            const status = error.response?.status || 500;
            const message = error.response?.data || "Error fetching data";
            return NextResponse.json({ error: message }, { status });
        }
    } catch (error) {
        console.error("Error in GET handler:", error);
        return NextResponse.json({ error: "Internal server error" }, { status: 500 });
    }
}

