package nl.saxion;

import org.omg.CORBA.SetOverrideType;
import robocode.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Kang extends TeamRobot {
    boolean isLeaderMode = false;
    private byte scanDirection = 1;

    public void run(){
        //TODO receive message;
       //TODO LeaderSwitch ls;

        double headDgrees = getHeading();
        if(headDgrees != 0 && headDgrees < 0){

            setTurnRight(-headDgrees);
        } else {
            setTurnLeft(headDgrees);
        }

        while(!isLeaderMode){
            /**
             * send enegry
             * ls = new LeaderSwitch(3,LeaderSwitch.energy)
             * */
            scanDirection *= -1;
            setAdjustRadarForRobotTurn(false);
            setTurnGunRight(360 * scanDirection);
            //move forward 100
            //if((getX() >= 100 && getX() <= getBattleFieldWidth() - 100) && (getY() >= 100 && getY() <= getBattleFieldHeight() - 100)) {
                setAhead(100);
                waitFor(new MoveCompleteCondition(this));
                //move back 100
                setBack(100);
                waitFor(new MoveCompleteCondition(this));}
 /** {
                double degree = Math.abs(getBattleFieldHeight() /2 - getY()) / Math.sqrt(Math.pow((getBattleFieldHeight() /2 - getY()),2) + Math .pow((getBattleFieldWidth()/2 - getX()),2));
               if(getX() > getBattleFieldWidth() /2  && getY() > getBattleFieldHeight()/2){
                   //第一象限
                   setTurnLeft(90 + degree);
               } else if (getX() < getBattleFieldWidth() /2 && getY() > getBattleFieldHeight()/2) {
                   //第二象限
                   setTurnRight(90 + degree);
               } else if (getX() < getBattleFieldWidth() /2 && getY() < getBattleFieldHeight()/2){
                   //第三象限
                   setTurnRight(90 - degree);
               } else {
                   //第四象限
                   setTurnLeft(90 - degree);
               }
               setAhead(Math.sqrt(Math.pow((getBattleFieldHeight() / 2 - 100),2) + Math.pow((getBattleFieldWidth() / 2 - 100) ,2)));
            }

  */
               while(isLeaderMode){
                   //TODO switch to LeaderMode
               }
         }


    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        if(event.getDistance() < 150) {
            fire(3);
        } else if(event.getDistance() >= 150 && event.getDistance() < 200){
            fire(2);
        }

    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        changeDirection();
    }

    @Override
    public void onHitWall(HitWallEvent event) {
       changeDirection();
    }


    @Override
    public void onHitRobot(HitRobotEvent event) {
        double gunTurnAmt = normalRelativeAngleDegrees(event.getBearing() + (getHeading() - getRadarHeading()));
        setTurnGunRight(gunTurnAmt);
        changeDirection();
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        changeDirection();
    }

    public void changeDirection(){
        double headDgrees = getHeading();
        out.println(headDgrees);
        if(headDgrees == 90) {
            if(getY() >= getBattleFieldHeight() / 2) {
                setTurnLeft(headDgrees);
            }else {
                setTurnRight(headDgrees);
            }
            out.println(headDgrees);
        }else if(headDgrees == 270){
            if(getY() >= getBattleFieldHeight() / 2) {
                setTurnRight(90);
            } else {
                setTurnLeft(90);
            }
        } else if (headDgrees == 0){
            if(getX() <= getBattleFieldWidth() / 2){
                setTurnLeft(90);
            } else {
                setTurnRight(90);
            }
        } else if (headDgrees == 180 ||headDgrees == -180){
            if(getX() <= getBattleFieldWidth() / 2){
                setTurnRight(90);
            } else {
                setTurnLeft(90);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        //TODO
        /**
        if(event.getMessage() instanceof LeaderSwitch){
         LeaderSwitch ls = (LeaderSwitch) event.getMessage();
         }

         isLeadership =  ls.isLeaderAlive
         */
    }
}
