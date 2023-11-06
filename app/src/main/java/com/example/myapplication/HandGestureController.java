package com.example.myapplication;


import android.os.Debug;
import android.util.Log;

import com.google.mediapipe.formats.proto.LandmarkProto;

import java.util.List;

public class HandGestureController {

    private static final String INFO = "CHERRY-INFO";
    private static final String DEBUG = "CHERRY-DEBUG";
    private MainActivity mainActivity;

    private List<LandmarkProto.NormalizedLandmarkList> handLandMarkList;
    private double xNavigation ;
    private int xNavigationLeft ;
    private int xNavigationRight;
    private int xNavigationCount ;
    private double yNavigation ;
    private int yNavigationDown ;
    private int yNavigationUp ;
    private int yNavigationCount;
    private int okCount;
    public HandGestureController(MainActivity mainActivity){
        this.xNavigation = 0.0;
        this.xNavigationLeft = 0;
        this.xNavigationRight = 0;
        this.xNavigationCount = 0;
        this.yNavigation = 0.0;
        this.yNavigationDown = 0;
        this.yNavigationUp = 0;
        this.yNavigationCount = 0;
        this.okCount = 0;
        this.mainActivity = mainActivity;
    }


    public boolean handPresence()
    {
        return handLandMarkList.isEmpty() ? false : true;
    }
    public int handCount()
    {
        return handLandMarkList.size();
    }

    private  boolean OkGesture()
    {
        if(handLandMarkList.get(0).getLandmarkList().get(4).getY() > handLandMarkList.get(0).getLandmarkList().get(8).getY()
                && handLandMarkList.get(0).getLandmarkList().get(4).getY() > handLandMarkList.get(0).getLandmarkList().get(0).getY())
        {
            return true;
        }
        return false;
    }

    private  void ResetOk()
    {
        okCount = 0;
    }

    private  void NavigateOk()
    {
        //System.out.println("Checking Okay");
        ResetUpDown();
        ResetLeftRight();
        okCount++;
        if(okCount == 20){
            ResetOk();
            Log.i(DEBUG,"Clicked");
            mainActivity.sendMessage("pressed ok");

        }
    }

    private  void ResetUpDown()
    {
        yNavigationUp = 0;
        yNavigationDown = 0;
        yNavigationCount = 0;
        yNavigation = 0.0;

    }

    private boolean UpDownGesture()
    {
        if(handLandMarkList.get(0).getLandmarkList().get(16).getY() < handLandMarkList.get(0).getLandmarkList().get(13).getY()
                &&
                handLandMarkList.get(0).getLandmarkList().get(20).getY() < handLandMarkList.get(0).getLandmarkList().get(17).getY()
                &&
                handLandMarkList.get(0).getLandmarkList().get(8).getY() > handLandMarkList.get(0).getLandmarkList().get(5).getY())
        {
            return true;
        }
        return false;
    }

    private void NavigateUpDown()
    {
        //System.out.println("Checking Up and Down Gestures");
        ResetLeftRight();
        ResetOk();
        double point_y = handLandMarkList.get(0).getLandmarkList().get(5).getY();
        //Log.i(DEBUG,"Point Y is: "+point_y);
        double y_lo = yNavigation;
        double per_change = ((Math.abs(y_lo-point_y))/y_lo)*100;
        if(y_lo != 0.0 && point_y > y_lo && per_change > 5.0)
        {
            yNavigationDown = 0;
            yNavigationUp++;
        }
        else if(y_lo != 0.0 && point_y < y_lo && per_change > 5.0)
        {
            yNavigationUp = 0;
            yNavigationDown++;
        }
        yNavigationCount++;
        yNavigation = point_y;
        //System.out.println(CameraFragment.y_count);
        if(yNavigationCount == 5)
        {
            Integer up = yNavigationUp;
            Integer down = yNavigationDown;

            if( up > down)
            {
                Log.i(DEBUG,"Moved up");
                //mainActivity.sendMessage("moving upwards");
                mainActivity.sendMessage("moving upwards");
            }
            else if( up < down)
            {
                Log.i(DEBUG,"Moved Down");
                //mainActivity.sendMessage("{gesture"+":"+"moving downwards}");
                mainActivity.sendMessage("moving downwards");

            }
            ResetUpDown();
        }

    }

    private void ResetLeftRight()
    {
        xNavigationLeft = 0;
        xNavigationRight = 0;
        xNavigationCount = 0;
        xNavigation = 0.0;
    }

    private void NavigateLeftRight()
    {
        ResetUpDown();
        ResetOk();
        double point_x = handLandMarkList.get(0).getLandmarkList().get(20).getX();
        //System.out.println(point_x);
        //double x_distance = FindDistance(0.0, 0.0, point_x, 0.0);
        double x_lo=xNavigation;
        //System.out.println(x_distance);
        double per_change = ((Math.abs(x_lo-point_x))/x_lo)*100;
        if( x_lo !=0.0 && point_x > x_lo && per_change > 5.0 )
        {
            xNavigationRight = 0;
            xNavigationLeft++;
        }
        else if ( x_lo !=0.0 && point_x < x_lo && per_change > 17.5)
        {
            xNavigationLeft = 0;
            xNavigationRight++;
        }
        xNavigationCount++;
        xNavigation = point_x;
        if(xNavigationCount == 20)
        {
            Integer left = xNavigationLeft;
            Integer right = xNavigationRight;
            if(left > right)
            {
                Log.i(DEBUG,"Moved Left");
                //mainActivity.sendMessage("hand left moved");
                mainActivity.sendMessage("hand left moved");
            }
            else if(right > left)
            {
                Log.i(DEBUG,"Moved Right");
               mainActivity.sendMessage("hand right moved");
            }

            ResetLeftRight();
        }
    }

    private boolean LeftRightGesture()
    {
        if(     handLandMarkList.get(0).getLandmarkList().get(8).getY()  > handLandMarkList.get(0).getLandmarkList().get(5).getY()  &&
                handLandMarkList.get(0).getLandmarkList().get(12).getY() > handLandMarkList.get(0).getLandmarkList().get(9).getY()  &&
                handLandMarkList.get(0).getLandmarkList().get(16).getY() > handLandMarkList.get(0).getLandmarkList().get(13).getY() &&
                handLandMarkList.get(0).getLandmarkList().get(20).getY() > handLandMarkList.get(0).getLandmarkList().get(17).getY()) {
            //Log.i(DEBUG,"HAND is UP and ready for Left-right Navigation");
            return true;
        }
        else
        {
            //Log.i(DEBUG,"HAND is Down and not ready for Left-right Navigation");
            return false;
        }
    }

    private void StartModels()
    {
        if(LeftRightGesture()==true) {
            NavigateLeftRight();
            //Log.i(DEBUG, "Looking for Left-Right gesture");
        }
        else if(UpDownGesture()==true)
        {
            NavigateUpDown();
            //Log.i(DEBUG, "Looking for Up_Down gesture");
        }
        else if(OkGesture()==true)
        {
            NavigateOk();
        }

    }

    public void ProcessGestureRecognition(List<LandmarkProto.NormalizedLandmarkList> handLandmarks)
    {
        handLandMarkList = handLandmarks;
        if(handPresence() == true) {
            //Log.i(DEBUG,handCount()+"Hands Detected");
            //Log.i(INFO,"X-Axis point"+"="+handLandmarks.)
            //Log.i(INFO, "Coordinate for Thumb"+"X:"+handLandMarkList.get(0).getLandmark(0).getX());
            StartModels();
        }
    }

}
