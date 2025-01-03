
import React from 'react';
import '../app/styles/hotel.css';
import Image from 'next/image';
function HotelInfo() {
  return (
    <div className="hotel-info-container">
      <div className="hotel-content">
        <div className="hotel-image">
          <Image
            src="/images/hotel-image.jpg"  // Ruta relativa desde 'public'
            alt="Image del hotel"
            layout="fill"
          />
        </div>
        <div className="hotel-details">
          <h2>About Us</h2>
          <p>
            Welcome to Hotel Paradise, established in 1995. Our vision is to be the leading hospitality provider, creating memorable experiences for all our guests. Our mission is to deliver exceptional service and comfort through innovation and dedication.
          </p>
          <h3>Our Values:</h3>
          <ul>
            <li>Customer Satisfaction</li>
            <li>Integrity</li>
            <li>Innovation</li>
            <li>Sustainability</li>
          </ul>
          <h3>Key Facts:</h3>
          <ul>
            <li>Founded: 1995</li>
            <li>Location: Tropical Paradise, World</li>
            <li>Rooms: 200 luxury suites</li>
            <li>Awards: Best Hotel of the Year 2020</li>
          </ul>
        </div>
      </div>
    </div>
  );
}

export default HotelInfo;