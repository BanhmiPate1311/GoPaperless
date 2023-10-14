import { useSelector } from "react-redux";

export const useParticipantControllerManager = () =>
  useSelector((state) => state.participantControllerManagerReducer);
