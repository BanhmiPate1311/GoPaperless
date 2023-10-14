import React from "react";
import MobileId from "./smartId/MobileId";
import ViettelCA from "./smartId/ViettelCA";

const FieldSmartId = ({ isCardChecked, connectorName, workFlow }) => {
  return (
    <div>
      {(connectorName === "SMART_ID_MOBILE_ID" ||
        connectorName === "SMART_ID_LCA") && (
        <MobileId
          isCardChecked={isCardChecked}
          connectorName={connectorName}
          workFlow={workFlow}
        />
      )}
      {connectorName === "SMART_ID_VIETTEL-CA" && (
        <ViettelCA
          isCardChecked={isCardChecked}
          connectorName={connectorName}
          workFlow={workFlow}
        />
      )}
    </div>
  );
};

export default FieldSmartId;
