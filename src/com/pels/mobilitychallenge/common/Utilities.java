package com.pels.mobilitychallenge.common;

import android.util.Log;

public class Utilities {
	  private static final int LOGLEVEL = 5;

	    private static void LogToDebugFile(String message)
	    {
	        if (Session.isDebugToFile())
	        {
	            DebugLogger.Write(message);
	        }
	    }

	    public static void LogInfo(String message)
	    {
	        if (LOGLEVEL >= 3)
	        {
	            Log.i("GPSLogger", message);
	        }

	        LogToDebugFile(message);

	    }

	    public static void LogError(String methodName, Exception ex)
	    {
	        try
	        {
	            LogError(methodName + ":" + ex.getMessage());
	        }
	        catch (Exception e)
	        {
	            /**/
	        }
	    }

	    private static void LogError(String message)
	    {
	        Log.e("GPSLogger", message);
	        LogToDebugFile(message);
	    }

	    @SuppressWarnings("unused")
	    public static void LogDebug(String message)
	    {
	        if (LOGLEVEL >= 4)
	        {
	            Log.d("GPSLogger", message);
	        }
	        LogToDebugFile(message);
	    }

	    public static void LogWarning(String message)
	    {
	        if (LOGLEVEL >= 2)
	        {
	            Log.w("GPSLogger", message);
	        }
	        LogToDebugFile(message);
	    }

	    @SuppressWarnings("unused")
	    public static void LogVerbose(String message)
	    {
	        if (LOGLEVEL >= 5)
	        {
	            Log.v("GPSLogger", message);
	        }
	        LogToDebugFile(message);
	    }
}
