import React, { Fragment } from "react";
import Vietnam from "./ElectronicId/Vietnam";
import Vietnam1 from "./ElectronicId/Vietnam1";

const FieldElecId = ({ isCardChecked, connectorName, workFlow, swError }) => {
  return (
    <Fragment>
      {isCardChecked === 0 && (
        <Vietnam
          connectorName={connectorName}
          workFlow={workFlow}
          swError={swError}
        />
      )}
      {/* {isCardChecked === 0 && (
        <Vietnam1
          connectorName={connectorName}
          workFlow={workFlow}
          swError={swError}
        />
      )} */}
    </Fragment>
  );
};

export default FieldElecId;
