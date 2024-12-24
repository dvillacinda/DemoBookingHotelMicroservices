'use client';

import './globals.css';
import { Inter } from 'next/font/google';
import AuthStatus from "../components/authStatus";
import SessionProviderWrapper from '../utils/sessionProviderWrapper';
import SideBar from '@/components/SideBar';
import { useState } from 'react';
import ServerLayout from './server-layout';
const inter = Inter({ subsets: ['latin'] });

const links = ["/", "/information/"];
const items = ["home", "get all"];

export default function RootLayout({ children }: { children: React.ReactNode }) {
  const [isOpen, setStatus] = useState(false);

  const handleOpen = () => {
    setStatus(true);
  };

  const handleClose = () => {
    setStatus(false);
  };

  return (
    <SessionProviderWrapper>
      <html lang="en">
        <body className={inter.className}>
          <header className="w-full p-3 bg-gray-700 flex justify-between items-center">
            <div className="flex-1"></div>
            <h1 className="text-white text-2xl text-center flex-1">Hotel Paradise</h1>
            <div className="flex-1 text-right">
              <AuthStatus />
            </div>
          </header>

          <ServerLayout>
            <div className="flex flex-row">
              <div className="w-1/6 p-3 h-screen bg-gray-700">
                <img src="/favicon.ico" alt="icon" style={{ width: "150px", height: "150px", alignContent: "center" }} />
                <SideBar
                  items={items}
                  links={links}
                  onClose={handleClose}
                  openClick={handleOpen}
                  isOpen={isOpen}
                />
              </div>
              <div className="w-4/5 p-3 h-screen bg-black">{children}</div>

            </div>
          </ServerLayout>
        </body>
      </html>
    </SessionProviderWrapper>
  );
}
