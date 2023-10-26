import { api } from "../constants/api";

export const smartIdService = {
  sign: (data, signal) => {
    return api.post("/signFile", data, { signal });
  },
};
