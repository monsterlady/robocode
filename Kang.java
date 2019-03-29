package nl.saxion;

import robocode.*;
import java.io.IOException;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Kang extends TeamRobot {

    private byte moveDirection = 1;
    public void run() {
        setMoveDirection();
        setAdjustRadarForRobotTurn(false);
       while(true) {
           setTurnRadarRight(360000);
           keepingMoving();
       }
    }
    public void keepingMoving(){
        setAhead(100);
        waitFor(new MoveCompleteCondition(this));
        //move back 100
        setBack(100);
        waitFor(new MoveCompleteCondition(this));
    }
    public void setMoveDirection(){
        double headDgrees = getHeading();
        if (headDgrees != 0 && headDgrees < 0) {
            setTurnRight(-headDgrees);
        } else {
            setTurnLeft(headDgrees);
        }
    }
    public void backtoBattle(){
        double degree = Math.asin(Math.abs(getBattleFieldHeight() /2 - getY()) / Math.sqrt(Math.pow((getBattleFieldHeight() /2 - getY()),2) + Math .pow((getBattleFieldWidth()/2 - getX()),2)));
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
    public void changeDirection(){
        double headDgrees = getHeading();
        //out.println(headDgrees);
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
    public void doMove(double bearing){
        if (getVelocity() == 0)
            moveDirection *= -1;
        // always square off against our enemy
        setTurnRight(bearing + 90);
    }
    private void attack(double distance){
        if(distance < 150) {
            fire(3);
        } else if(distance >= 150 && distance < 300){
            fire(2);
        } else {
            fire(1);
        }
    }
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        if (isTeammate(event.getName())) {
            return;
        }

        double enemyBearing = this.getHeading() + event.getBearing();
        double locationX = getX() + event.getDistance() * Math.sin(Math.toRadians(enemyBearing));
        double locationY = getY() + event.getDistance() * Math.cos(Math.toRadians(enemyBearing));

        try {
            // Send enemy position to teammates
            broadcastMessage(new Vector(locationX, locationY));
        } catch (IOException ex) {
            out.println("Unable to send order: ");
            ex.printStackTrace(out);
        }
        double distanceX = locationX - this.getX();
        double distanceY = locationY - this.getY();

        double angle = Math.toDegrees(Math.atan2(distanceX, distanceY));

        Vector enemyPos = new Vector(distanceX, distanceY);
        setTurnRadarRight(getHeading() - getRadarHeading() + event.getBearing());
        turnGunRight(normalRelativeAngleDegrees(angle - getGunHeading()));
        doMove(event.getBearing());
        attack(enemyPos.length());

    }

           /** if (corner.getX() == enemyPos.getX() && corner.getY() == enemyPos.getY()) {
                setTurnRadarRight(getHeading() - getRadarHeading() + event.getBearing());
                turnGunRight(normalRelativeAngleDegrees(angle - getGunHeading()));
                doMove(event.getBearing());
                attack(50);
            } else {
                setTurnRadarRight(getHeading() - getRadarHeading() + event.getBearing());
                turnGunRight(normalRelativeAngleDegrees(angle - getGunHeading()));
                doMove(event.getBearing());
                attack(enemyPos.length());
            }
            corner = new Vector(enemyPos.getX(), enemyPos.getY());
        }*/
    

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        changeDirection();

    }

    @Override
    public void onHitWall(HitWallEvent event) {
        setMoveDirection();
        backtoBattle();
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


}
