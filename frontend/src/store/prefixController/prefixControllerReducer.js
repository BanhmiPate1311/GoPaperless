import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "../../constants/api";

const initialState = {
  prefix: [],
  prefixPhone: [],
  connectorNames: [],
  isFetchingPreFix: false,
  error: undefined,
};

export const {
  reducer: prefixControllerReducer,
  actions: prefixControllerActions,
} = createSlice({
  name: "prefixController",
  initialState,
  // Xử lý action đồng bộ
  reducers: {},
  // Xử lý action bất đồng bộ
  extraReducers: (builder) => {
    builder

      // getPrefixPhone
      .addCase(getPrefixPhone.pending, (state, action) => {
        // state.isFetchingPreFix = true;
      })
      .addCase(getPrefixPhone.fulfilled, (state, action) => {
        // state.isFetchingPreFix = false;
        state.prefixPhone = action.payload;
      })
      .addCase(getPrefixPhone.rejected, (state, action) => {
        // state.isFetchingPreFix = false;
        // state.error = action.payload;
      })

      // getPrefix
      .addCase(getPrefix.pending, (state, action) => {
        // state.isFetchingPreFix = true;
      })
      .addCase(getPrefix.fulfilled, (state, action) => {
        // state.isFetchingPreFix = false;
        state.prefix = action.payload;
      })
      .addCase(getPrefix.rejected, (state, action) => {
        // state.isFetchingPreFix = false;
        // state.error = action.payload;
      })

      // getConnectorName
      .addCase(getConnectorName.pending, (state, action) => {
        // state.isFetching = true;
      })
      .addCase(getConnectorName.fulfilled, (state, action) => {
        // state.isFetching = false;
        state.connectorNames = action.payload;
      })
      .addCase(getConnectorName.rejected, (state, action) => {
        // state.isFetching = false;
        // state.error = action.payload;
      });
  },
});

export const getPrefixPhone = createAsyncThunk(
  "prefixController/getPrefixPhone",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await api.post("/getPrefixPhone", { lang: data });
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const getPrefix = createAsyncThunk(
  "prefixController/getPrefix",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await api.post("/getPrefix", { lang: data });
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const getConnectorName = createAsyncThunk(
  "prefixController/getConnectorName",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await api.post("/base64Logo", {
        param: data,
      });
      // console.log("ConnectorName: ", response.data);
      return response.data;
    } catch (error) {
      console.log("error: ", error);
      return rejectWithValue(error.response.data);
    }
  }
);
