import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { electronicService } from "../../services/electronicService";

const initialState = {
  isSignSuccess: "",
  subject: null,
  isIdentifyRegistered: false,
  personalInfomation: null,
  image: null,
  error: null,
  isFetching: false,
};

export const {
  reducer: electronicControllerManagerReducer,
  actions: electronicControllerManagerActions,
} = createSlice({
  name: "electronicControllerManager",
  initialState,
  // Xử lý action đồng bộ
  reducers: {
    setMessageSuccess(state) {
      state.isSignSuccess = "Document signed successfully.";
    },
    clearsetMessageSuccess(state) {
      state.isSignSuccess = "";
    },
    setPersonalInformation(state, action) {
      state.personalInfomation = action.payload;
    },
    setImage(state, action) {
      state.image = action.payload;
    },
    // setMessageError(state, action) {
    //   state.messageError = action.payload;
    // },
    // clearsetMessageError(state) {
    //   state.messageError = "";
    // },
    // setIsLoading(state, action) {
    //   state.isLoading = action.payload;
    // },
  },

  // Xử lý những action bất đồng bộ (call API)
  extraReducers: (builder) => {
    builder
      // checkUserIdentity
      .addCase(checkUserIdentity.pending, (state, action) => {
        state.isFetching = true;
      })
      .addCase(checkUserIdentity.fulfilled, (state, action) => {
        if (action.payload.status === 0) {
          state.isIdentifyRegistered = true;
          state.personalInfomation = action.payload.personal_informations;
          state.image = action.payload.personal_informations.dg2;
        }
        state.subject = action.payload;
        state.isFetching = false;
      })
      .addCase(checkUserIdentity.rejected, (state, action) => {
        state.error = action.payload;
        state.isFetching = false;
      });
  },
});

export const checkUserIdentity = createAsyncThunk(
  "electronicControllerManager/checkUserIdentity",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await electronicService.checkIdentity(data);

      return response.data;
    } catch (error) {
      console.log("error: ", error);
      return rejectWithValue(error.response.data);
    }
  }
);
