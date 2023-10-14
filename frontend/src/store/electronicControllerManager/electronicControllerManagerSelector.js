import { useSelector } from "react-redux";

export const useElectronicControllerManager = () =>
  useSelector((state) => state.electronicControllerManagerReducer);
