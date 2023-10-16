import { api } from "../constants/api";

export const smartIdService = {
  sign: (data) => {
    return api.post("/signFile", data);
  },
};
