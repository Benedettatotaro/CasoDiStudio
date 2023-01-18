package com.example.casodistudio.game.gameviews;

import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioY;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Update {
    private ViewTravel viewTravel;
    public Update(ViewTravel viewTravel){
        this.viewTravel=viewTravel;
    }
    public void update(){
        viewTravel.background_1.y+=10*screenRatioY;
        viewTravel.background_2.y+=10*screenRatioY;
        if(viewTravel.background_1.y>viewTravel.screenY){ //se il background è sceso fuori dallo schermo
            viewTravel.background_1.y=-viewTravel.screenY;
        }
        if(viewTravel.background_2.y>viewTravel.screenY){ //se il background è sceso fuori dallo schermo
            viewTravel.background_2.y=-viewTravel.screenY;
        }

        if(viewTravel.gameCounter%100==0){
            viewTravel.generateEnemies();
            viewTravel.generatePowerup();
        }

        List<Bullet> trash= new ArrayList<>();
        List<Enemy> enemiesTrash=new ArrayList<>();

        for(Bullet bullet:viewTravel.bullets) {
            if (bullet.y < 0)   //questo vuol dire che il proiettile è nello schermo va aggiunto alla lista dei proiettili trash
                trash.add(bullet);
            bullet.y -= 50 * screenRatioY;  //muovere il proiettile di 50 pixel

            if(!viewTravel.enemies.isEmpty()){
                for(Enemy enemy:viewTravel.enemies){
                    if(Rect.intersects(enemy.getCollisionShape(),bullet.getCollisionShape())){ //se un proiettile colpisce l'asteroide
                        if(enemy.isGem){
                            enemy.gemShot=true;
                            enemy.xGem=enemy.x+enemy.enemy.getWidth()/2-enemy.gem.getWidth()/2;
                            enemy.yGem=enemy.y+enemy.enemy.getHeight()/2-enemy.gem.getHeight()/2;
                        }
                        enemy.y-=viewTravel.screenY+500; //l'asteroide si sposta di 500+screenY
                        bullet.y=-500;
                    }
                }
            }

        }

        for(Bullet bullet:trash){
            viewTravel.bullets.remove(bullet);
        }

        if(viewTravel.ship.isPowerup&&viewTravel.ship.yPowerup<=viewTravel.screenY){   //controllo per la velocità casuale del power up
            viewTravel.ship.yPowerup+=viewTravel.ship.speedPowerup;
            int bound=(int)(30*screenRatioY);
            viewTravel.ship.speedPowerup=viewTravel.random.nextInt(bound);
            if(viewTravel.ship.speedPowerup<10*screenRatioY){
                viewTravel.ship.speedPowerup=(int) (10*screenRatioY);
            }
            if(Rect.intersects(viewTravel.ship.getCollisionShapePowerUp(),viewTravel.ship.getCollisionShape())){  //se la navicella colpisce il powerup
                viewTravel.ship.isPoweringup=true; //cambia la bitmap della navicella
                viewTravel.ship.yPowerup+=viewTravel.screenY;
                viewTravel.ship.counterPouwerup=viewTravel.gameCounter; //inizializza il contatore del powerup
            }
        }

        if(viewTravel.ship.isPoweringup&&viewTravel.gameCounter>=viewTravel.ship.counterPouwerup+200){  //se il contatore di gioco incrementa di 833 cioè all'incirca 30 secondi
            viewTravel.ship.isPoweringup=false;  //termina il power up
        }

        else if(viewTravel.ship.isPowerup&&viewTravel.ship.yPowerup>=viewTravel.screenY){  //se il power up esce dallo schermo la variabile cambia
            viewTravel.ship.isPowerup=false;
        }

        if(!viewTravel.enemies.isEmpty()){
            for(Enemy enemy:viewTravel.enemies){ //per ogni asteroide
                enemy.y+=enemy.speed; //assegna alla y una certa velocità
                if(enemy.y<viewTravel.screenY){  //finchè l'asteroide rimane sullo schermo aumenta la sua posizione lungo l'asse delle y
                    int bound=(int) (30*screenRatioY);
                    enemy.speed=viewTravel.random.nextInt(bound);
                    if(enemy.speed<10*screenRatioY){
                        enemy.speed=(int)(10*screenRatioY);
                    }
                }
                else{
                    enemiesTrash.add(enemy);
                }
                if(Rect.intersects(enemy.getCollisionShape(),viewTravel.ship.getCollisionShape())){
                    if(viewTravel.ship.isPoweringup){  //se è il powerup non perdi
                        enemy.y-=viewTravel.screenY+500; //l'asteroide si sposta di 500+screenY
                    }
                    else{
                        viewTravel.isGameOver=true;
                        return;
                    }
                }
                if(enemy.isGem&&enemy.gemShot&&enemy.yGem<=viewTravel.screenY){ //se l'elemento dell'array list era un asteroide con gemma ed è stato colpito quindi c'è la gemma sullo schermo e non è ancora scomparsa
                    enemy.yGem+=enemy.speed;
                    if(Rect.intersects(enemy.getCollisionShapeGem(),viewTravel.ship.getCollisionShape())){
                        viewTravel.gemCounter++;
                        enemy.yGem+=viewTravel.screenY; //fa scomparire la gemma
                    }
                }
                else{  //quando la gemma scompare dallo schermo la variabile torna falsa così da non farla disegnare più
                    enemy.gemShot=false;
                }

            }
        }

        if(!enemiesTrash.isEmpty()){
            for(Enemy enemy:enemiesTrash){
                viewTravel.enemies.remove(enemy);
            }
        }
    }
}
