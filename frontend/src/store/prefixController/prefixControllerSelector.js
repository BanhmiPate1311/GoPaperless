import { useSelector } from "react-redux";

export const usePrefixController = () =>
  useSelector((state) => state.prefixControllerReducer);
