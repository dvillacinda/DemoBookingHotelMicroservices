import axios from "axios";
import { NextResponse } from "next/server";
import { getAccessToken } from "../../../../utils/sessionTokenAccessor";
import { getServerSession } from "next-auth";
import { authOptions } from "../../auth/[...nextauth]/route";
import type { NextRequest } from "next/server";

export async function GET(req: NextRequest): Promise<Response> {
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


        // Obtener los parámetros startString y endString desde la URL
        const { searchParams } = new URL(req.url);
        const startString = searchParams.get("startString");
        const endString = searchParams.get("endString");

        if (!startString || !endString) {
            return NextResponse.json({ error: "Missing startString or endString parameters" }, { status: 400 });
        }

        const url = `${process.env.DEMO_BACKEND_URL}/api/reservation/get-available-rooms`;

        try {
            const accessToken = await getAccessToken();

            const fullUrl = `${url}?startString=${startString}&endString=${endString}`;
            const response = await axios.get(fullUrl, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
                withCredentials: true,
            });
            return NextResponse.json({ data: response.data }, { status: response.status });
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

