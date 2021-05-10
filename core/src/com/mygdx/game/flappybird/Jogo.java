package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {

	private int movimentaY = 0;
	private int movimentaX = 0;
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoalto;
	private Texture canobaixo;


	private float larguraDispositivo;
	private float alturaDispositivo;
	private float variacao = 0;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro= 0;

	private float posicaocano;
	private float alturacano = 0;






	@Override
	public void create ()
	{
		batch = new SpriteBatch();
//lista de sprites do passaro
		passaros= new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

//setando as imagens
		fundo = new Texture("fundo.png");

		canoalto = new Texture("cano_topo.png");

		canobaixo = new Texture("cano_baixo.png");


// puxando da biblioteca para adaptar o fundo com o celular
		larguraDispositivo= Gdx.graphics.getWidth();
		alturaDispositivo= Gdx.graphics.getHeight();
//fazer passaro nascer no meio da tela
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;
//posicionando o cano
		posicaocano = (larguraDispositivo / 2) * 2;
//setando a altura do cano
		alturacano = alturaDispositivo - posicaoInicialVerticalPassaro / 2;




	}

	@Override
	public void render () {
// inicia renderização
		batch.begin();
		if (variacao > 3)

			variacao = 0;
// fazer o passaro subir ao tocar na tela
		boolean toqueTela = Gdx.input.justTouched();
		if (Gdx.input.justTouched()) {
			gravidade = -25;
		}
		if (posicaoInicialVerticalPassaro > 0 || toqueTela)
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

// puxa a imagem que colocou no create
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 30, posicaoInicialVerticalPassaro);
//spawn do cano e movimentação
		batch.draw(canoalto , posicaocano - movimentaX, alturacano, 100, 900);
		batch.draw(canobaixo , posicaocano - movimentaX, 0, 100, 900);

		variacao += Gdx.graphics.getDeltaTime() * 10;
		//adiciona gravidade
		gravidade++;
		// adiciona valor no eixo y, fazendo ele movimentar neste eixo
		movimentaY++;
		// adiciona valor no eixo x, fazendo ele movimentar neste eixo
		movimentaX++;
		//termina a sequencia da aplicação
		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
