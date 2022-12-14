package com.dnlStudios.enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dnlStudios.entities.Enemy;
import com.dnlStudios.main.Main;
import com.dnlStudios.world.Camera;
import com.dnlStudios.world.Hud;
import com.dnlStudios.world.World;

public class SlimeEnemy extends Enemy{
	
	protected double speed = 0.2;
	protected double maxSpeed = 0.8;
	protected int tickDistance = 90;
	
	protected int frames = 0, maxFrames = 10, index = 0, maxIndex = 2;
	protected int Fframes = 0, FmaxFrames = 15, Findex = 0, FmaxIndex = 2;
	//protected int enW,enH;
	//protected int maskX, maskY;
	
	protected BufferedImage[] enSpr;
	protected BufferedImage feedback = Main.spritesheet.getSprite(224, 16, 16, 16);

	public SlimeEnemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		setHealth(24);
		this.setCollisionMask(0, 0, 16, 16);
		this.enH = 16;
		this.enW =16;
		this.maskX = 0;
		this.maskY = 0;
		enSpr = new BufferedImage[3];
		int type = Main.rand.nextInt(4);
		for(int i = 0; i < 3; i++)
			enSpr[i] = Main.spritesheet.getSprite(176 + (i*16), 0 + (type*16), 16, 16);
	}
	public void tick() {
		if(isFrozen) {
			speed=0;
			maxSpeed=0;
		}else {
			speed=0.2;
			maxSpeed=0.8;
			speed = Main.rand.nextDouble(speed,maxSpeed);
		}
		collidingEntity();
		if(this.x >= Camera.x - tickDistance
			&& this.y >= Camera.y - tickDistance
			&& this.x <= Camera.x + Main.width +tickDistance
			&& this.y <= Camera.y + Main.height + tickDistance
			) {
			if(!isDamaged) {
				if(checkPlayerCollision() ==  false) {
					if(Main.rand.nextInt(100) < 60) {
						if((int)x < Main.player.getX() && World.isFree((int)(x+maskX+speed), (int)y+maskY,enW,enH)
								&& !this.isColliding((int)(x+speed), (int)y)
								) {
							x+= speed;
							
						}else if((int)x > Main.player.getX() && World.isFree((int)(x+maskX-speed), (int)y+maskY,enW,enH)
								&& !this.isColliding((int)(x-speed), (int)y)
								) {
							x	-= speed;
						}if((int)y < Main.player.getY() && World.isFree((int)(x+maskX), (int)(y +maskY + speed),enW,enH)
								&& !this.isColliding((int)(x), (int)(y + speed))
								) {
							y	+= speed;
						}else if((int)y > Main.player.getY() && World.isFree((int)(x+maskX), (int)(y +maskY - speed),enW,enH)
								&& !this.isColliding((int)(x), (int)(y - speed))
								) {
							y	-= speed;
						}
						if(!isFrozen) {
							frames++;
							if(frames == maxFrames) {
								frames = 0;
								index++;
								if(index > maxIndex) {
									index = 0;
								}
							}
						}else {
							Fframes++;
							if(Fframes == FmaxFrames) {
								Fframes = 0;
								Findex++;
								if(Findex > FmaxIndex) {
									Findex = 0;
									isFrozen=false;
								}
							}
						}
					}
				}else {
					if(Main.rand.nextInt(100) < 10) {
						Main.player.damagePlayer(10);
					}	
				}
			}
		}
		if(isDamaged) {
			dframes++;
			if(dframes == dmaxFrames) {
				dframes = 0;
				dindex++;
				if(dindex > dmaxIndex) {
					dindex = 0;
					this.isDamaged=false;
					this.invincibleFrame=false;
				}
			}
		}
	}
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(enSpr[index], this.getX() - Camera.x,this.getY()-Camera.y,null);
		}else {
			g.drawImage(feedback, this.getX() - Camera.x,this.getY()-Camera.y,null);
		}
		if(Hud.advancedHud == true) {
			g.setColor(Color.blue);
			g.fillRect(getX() + maskX - Camera.x, getY() + maskY - Camera.y, enW, enH);
			g.setColor(Color.red);
			g.fillRect(Main.player.getX() + Main.player.maskX - Camera.x, Main.player.getY() + Main.player.maskY - Camera.y, Main.player.pW, Main.player.pH);
		}
	}
}
