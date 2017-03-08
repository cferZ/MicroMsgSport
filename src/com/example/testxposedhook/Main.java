package com.example.testxposedhook;
import static de.robv.android.xposed.XposedHelpers.findClass;

import java.lang.reflect.Field;

import android.hardware.Sensor;
import android.util.Log;
import android.util.SparseArray;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookLoadPackage {
	private static int stepCount = 23333;//must lower than 98800
	private static int acclCount = 1;//accelerate sensor count
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		// TODO Auto-generated method stub
		Log.e("pack","pack = " + lpparam.packageName);
		if(!lpparam.packageName.equals("com.codoon.gps")){
			return;
		}
		final Class<?> sensorEL = findClass("android.hardware.SystemSensorManager$SensorEventQueue", lpparam.classLoader);

        XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent", new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
              //  ((float[]) param.args[1])[0] = ((float[]) param.args[1])[0] + 1168 * stepCount;
              //  stepCount++;
            	try{
            		 //先拿manager
	                Field mfield = param.thisObject.getClass().getSuperclass().getDeclaredField("mManager");
	                mfield.setAccessible(true);
	                Object mManager= mfield.get(param.thisObject);
	                //再拿mmanager下的sensor
	                Field field = param.thisObject.getClass().getEnclosingClass().getDeclaredField("mHandleToSensor");
	                field.setAccessible(true);
	                int handle = (Integer) param.args[0];
	                Sensor sensor = ((SparseArray<Sensor>) field.get(mManager)).get(handle);
	                
	                if(sensor.getType()==Sensor.TYPE_STEP_COUNTER){
//	                	Log.e("mytest","args = " + ((float[]) param.args[1])[0]+" "+ ((float[]) param.args[1])[1]+" "+ ((float[]) param.args[1])[2]+" "+ ((float[]) param.args[1])[3]);
		                ((float[]) param.args[1])[0] = stepCount;
		                Log.e("stepcounter","sensor = " + sensor);
            		}
	                if(sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
//	                	Log.e("mytest","args = " + ((float[]) param.args[1])[0]+" "+ ((float[]) param.args[1])[1]+" "+ ((float[]) param.args[1])[2]+" "+ ((float[]) param.args[1])[3]);
//		                ((float[]) param.args[1])[0] = stepCount;
		                Log.e("stepcounter","sensor = " + sensor);
            		}
	                else
	                	Log.e("othersensor","sensor = " + sensor);
//            		else{//accelerate sensor
//            			if(acclCount%100>=80){
//            				Log.e("mytest", "a loop end");
//            				acclCount++;
//            				return;
//            			}
//            			((float[]) param.args[1])[0] = 20;
//            			((float[]) param.args[1])[1] = 10;
//            			((float[]) param.args[1])[2] = 30;
//            			acclCount++;
//            			if(acclCount%20==0){
//            				Log.e("mytest","running accelerate");
//            			}
//            		}
	            }
            	catch(Exception e){
            		Log.e("mytest",Log.getStackTraceString(e)); 
            	}
            }
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//            	// TODO Auto-generated method stub
////            	super.afterHookedMethod(param);
//            	Field mfield = param.thisObject.getClass().getSuperclass().getDeclaredField("mManager");
//                mfield.setAccessible(true);
//                Object mManager= mfield.get(param.thisObject);
//                //再拿mmanager下的sensor
//                Field field = param.thisObject.getClass().getEnclosingClass().getDeclaredField("mHandleToSensor");
//                field.setAccessible(true);
//                int handle = (Integer) param.args[0];
//                Sensor sensor = ((SparseArray<Sensor>) field.get(mManager)).get(handle);
//                if(!sensor.getName().equals("Step Counter"))
//                	return;
//            	Log.e("mytest", "after hook : "+((float[])param.args[1])[0]);
//            }
        });

	}
}