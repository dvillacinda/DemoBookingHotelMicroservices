export const metadata = {
    title: 'My demo',
    description: 'Some description for my website',
};

export default function ServerLayout({ children }: { children: React.ReactNode }) {
    return <>{children}</>;
}
