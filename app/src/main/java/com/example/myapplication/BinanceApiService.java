package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface BinanceApiService {
	@GET("/api/v1/trades")
	Call<List<AggTrade>> getTrades(@Query("symbol") String symbol, @Query("limit") Integer limit);

	@GET("/api/v1/aggTrades")
	Call<List<AggTrade>> getAggTrades(@Query("symbol") String symbol, @Query("fromId") String fromId, @Query("limit") Integer limit,
	                                  @Query("startTime") Long startTime, @Query("endTime") Long endTime);
}
