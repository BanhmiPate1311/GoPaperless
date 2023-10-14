import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "../../constants/api";

const initialState = {
  signers: [],
  isFetchingParticipant: false,
  error: undefined,
};

export const {
  reducer: participantControllerManagerReducer,
  actions: participantControllerManagerActions,
} = createSlice({
  name: "participantControllerManager",
  initialState,
  // Xử lý action đồng bộ
  reducers: {},
  // Xử lý action bất đồng bộ
  extraReducers: (builder) => {
    builder
      .addCase(getParticipant.pending, (state, action) => {
        // state.isFetchingParticipant = true;
      })
      .addCase(getParticipant.fulfilled, (state, action) => {
        state.signers = action.payload;
        // state.isFetchingParticipant = false;
      })
      .addCase(getParticipant.rejected, (state, action) => {
        // state.error = action.payload;
        // state.isFetchingParticipant = false;
      });
  },
});

export const getParticipant = createAsyncThunk(
  "participantControllerManager/getParticipant",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await api.get("/participants");
      return response.data;
    } catch (error) {
      console.log("error: ", error);
      return rejectWithValue(error.response.data);
    }
  }
);
