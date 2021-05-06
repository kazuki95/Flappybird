package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture passaro;
	private Texture fundo;

	private float larguraDispositivo;
	private float alturaDispositivo;

	private  int movimentaY = 0;
	private  int movimentaX = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();

//puxando as imagens da pasta assets
		fundo = new Texture("fundo.png");
		passaro = new Texture("passaro1.png");

// puxando da biblioteca para adaptar o fundo com o celular
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();

	}

	@Override
	public void render () {
// inicia renderização
		batch.begin();
// puxa a imagem que colocar no create
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaro, movimentaX,movimentaY);

// adiciona valor no eixo x, fazendo ele movimentar neste eixo
		movimentaX ++;
// adiciona valor no eixo y, fazendo ele movimentar neste eixo
		movimentaY++;

//termina a sequencia da aplicação
		batch.end();

	}
	
	@Override
	public void dispose () {

	}
}
