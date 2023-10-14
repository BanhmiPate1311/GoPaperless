import React from "react";
import logo1 from "../assets/images/gopaperless_white.png";

export const Header = ({ logo, headerFooter }) => {
  return (
    <header
      className="panel-header"
      // style={{ background: colorHeader, zIndex: 3 }}
      style={{
        background:
          headerFooter !== null
            ? headerFooter.headerBackgroundColor
            : "-webkit-linear-gradient(right, #0a98e7 0%, #3e3a94 100%)",
        zIndex: 3,
      }}
    >
      <img
        src={logo ? logo : logo1}
        alt="Logo"
        height="53px"
        style={{ paddingLeft: "2.875em" }}
      />
    </header>
  );
};

export default Header;
