import { authOptions } from "../[...nextauth]/route";
import { getServerSession } from "next-auth";
import { getIdToken } from "@/utils/sessionTokenAccessor";

export async function GET(): Promise<Response> {
    try {
        const session = await getServerSession(authOptions);

        if (session) {
            const idToken = await getIdToken();

            if (!process.env.END_SESSION_URL || !process.env.NEXTAUTH_URL) {
                console.error("Missing environment variables for logout.");
                return new Response("Server configuration error", { status: 500 });
            }

            const url = `${process.env.END_SESSION_URL}?id_token_hint=${idToken}&post_logout_redirect_uri=${encodeURIComponent(process.env.NEXTAUTH_URL)}`;

            try {
                await fetch(url, { method: "GET" });
            } catch (err) {
                console.error("Error during Keycloak logout:", err);
                return new Response("Failed to log out user on Keycloak side", { status: 500 });
            }
        }

        return new Response(null, { status: 200 });
    } catch (error) {
        console.error("Error in GET handler:", error);
        return new Response("Internal server error", { status: 500 });
    }
}
