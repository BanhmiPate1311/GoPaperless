import { api } from "../constants/api";

export const validationService = {
  postBack: (data) => {
    return api.post("val/postback", data);
  },
  checkStatus: (data) => {
    return api.post("val/checkStatus", data);
  },
};
