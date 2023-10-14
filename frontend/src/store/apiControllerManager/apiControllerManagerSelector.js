import { useSelector } from "react-redux";

export const useApiControllerManager = () =>
  useSelector((state) => state.apiControllerManagerReducer);
