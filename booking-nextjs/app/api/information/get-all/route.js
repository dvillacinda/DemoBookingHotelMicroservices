import axios from "axios";
import { NextResponse } from "next/server";
import { getAccessToken } from "../../../../utils/sessionTokenAccessor";
import { getServerSession } from "next-auth";
import { authOptions } from "../../auth/[...nextauth]/route";

export async function GET(req) {
    const session = await getServerSession(authOptions);

    if (session) {
        const url = `${process.env.DEMO_BACKEND_URL}/api/info/get-all`; 

        try {
            const accessToken = await getAccessToken();


            const response = await axios.get(url, {
                headers: {
                    Authorization: `Bearer ${accessToken}`, 
                },
            });

            return NextResponse.json({ data: response.data }, { status: response.status });
        } catch (error) {

            const status = error.response?.status || 500;
            const message = error.response?.data || "Error to get the data";
            return NextResponse.json({ error: message }, { status });
        }
    }

    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
}
