package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Jogo extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;

	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;


	private float larguraDispositivo;
	private float alturaDispositivo;
	private float variacao = 0;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro = 0;
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;
	private float posicaoHorizontalPassaro = 0;

	private Random random;



	private int pontos = 0;
	private int estadoJogo = 0;
	private int pontuacaoMaxima = 0;

	private boolean passouCano = false;

	BitmapFont textoPontuacao;
	BitmapFont textoReiniciar;
	BitmapFont textoMelhorPontuacao;

	Sound somVoando;
	Sound somColisao;
	Sound somPontuacao;

	Preferences preferencias;


	@Override
	public void create ()
	{
		inicializaTexturas();
		inicializaObjetos();
	}

	@Override
	public void render ()
	{
		verificaEstadoJogo();
		desenharTexturas();
		detectarColisao();
		validarPontos();
	}

	private void inicializaObjetos()
	{
//inicializando objetos
		batch = new SpriteBatch();
		random = new Random();

// puxando da biblioteca para adaptar o fundo com o celular
		larguraDispositivo= Gdx.graphics.getWidth();
		alturaDispositivo= Gdx.graphics.getHeight();

//fazer passaro nascer no meio da tela
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;

//inicializando canos do jogo
		posicaoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 350;

//pontua????o do jogo
		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);
//Reiniciar o jogo
		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.RED);
		textoReiniciar.getData().setScale(3);
//Melhor pontua????o do jogo
		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.GOLDENROD);
		textoMelhorPontuacao.getData().setScale(3);

//colliders
		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoCima = new Rectangle();
		retanguloCanoBaixo = new Rectangle();
//som do jogo
		somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));
//salvar o valor da pontua????o maxima
		preferencias = Gdx.app.getPreferences("flappyBird");
		pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);


	}

	private void inicializaTexturas()
	{
//inicializando as imagens

//fazendo aparecer a lista de sprites do passaro
		passaros= new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

//fazendo aparecer o fundo
		fundo = new Texture("fundo.png");

//fazendo aparecer os canos
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");

//fazendo aparecer o Game Over
		gameOver = new Texture("game_over.png");

	}





	private void detectarColisao()
	{
//collider do passaro
		circuloPassaro.set(50 + passaros[0].getWidth() / 2,
				posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

//collider do cano de baixo
		retanguloCanoBaixo.set(posicaoCanoHorizontal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoBaixo.getWidth(), canoBaixo.getHeight());

//collider do cano de cima
		retanguloCanoCima.set(posicaoCanoHorizontal,
				alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
				canoTopo.getWidth(), canoTopo.getHeight());

//identificar se o passaro bateu nos canos
		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if(bateuCanoCima || bateuCanoBaixo)
		{
			if(estadoJogo == 1)
			{
				somColisao.play();
				estadoJogo = 2;
			}
		}
	}

	private void validarPontos()
	{
//verificar se passou o cano
		if(posicaoCanoHorizontal < 50 - passaros[0].getWidth())
		{
//se passou o cano adiciona ponto
			if(!passouCano)
			{
				pontos ++;
				passouCano = true;
				somPontuacao.play();
			}
		}
//varia????o da anima????o do passaro
		variacao += Gdx.graphics.getDeltaTime() * 10;
		if (variacao > 3)
			variacao = 0;
	}

	private void verificaEstadoJogo()
	{
		boolean toqueTela = Gdx.input.justTouched();
//inicia quando tocar na tela
		if(estadoJogo == 0)
		{
// fazer o passaro subir ao tocar na tela para iniciar o jogo
			if (toqueTela)
			{
				gravidade = -15;
				estadoJogo = 1;
				somVoando.play();
			}

		}
//verifica que o jogador est?? jogando
		else if (estadoJogo == 1)
		{

// fazer o passaro subir ao tocar na tela
			if (toqueTela)
			{
				gravidade = -15;
				somVoando.play();
			}

//velocidade dos canos
			posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;
//movimenta????o do cano
			if(posicaoCanoHorizontal < -canoTopo.getHeight())
			{
				posicaoCanoHorizontal = larguraDispositivo;
//randomizar a apari????o dos canos
				posicaoCanoVertical = random.nextInt(400) - 200;
				passouCano = false;
			}

			if (posicaoInicialVerticalPassaro > 0 || toqueTela)
				posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

//adiciona gravidade
			gravidade++;
		}
//jogador morreu
		else if (estadoJogo == 2)
		{
//salvar a nova pontua????o maxima
			if(pontos > pontuacaoMaxima)
			{
				pontuacaoMaxima = pontos;
				preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);
			}
//anima????o de quando o jogador bate no cano
			posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500;

//reiniciando os atributos para reiniciar o jogo quando o jogador perde
			if(toqueTela)
			{
				estadoJogo = 0;
				pontos = 0;
				gravidade = 0;
				posicaoHorizontalPassaro = 0;
				posicaoInicialVerticalPassaro = alturaDispositivo / 2;
				posicaoCanoHorizontal = larguraDispositivo;
			}
		}

	}

	private void desenharTexturas()
	{
// inicia renderiza????o
		batch.begin();

// puxa a imagem que colocou no create do fundo
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

// puxa a imagem que colocou no create do passaro
		batch.draw(passaros[(int) variacao], 50 + posicaoHorizontalPassaro, posicaoInicialVerticalPassaro);

// puxa a imagem que colocou no create dos canos
		batch.draw(canoBaixo, posicaoCanoHorizontal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);

// puxa a imagem que colocou no create dos pontos
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo -110);


		if(estadoJogo == 2)
		{
// puxa a imagem que colocou no create do Game Over
			batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);

// puxa a imagem que colocou no create do reiniciar
			textoReiniciar.draw(batch, "TOQUE NA TELA PARA REINICIAR!",
					larguraDispositivo / 2 - 350, alturaDispositivo / 2 - gameOver.getHeight() / 2 - 50);

// puxa a imagem que colocou no create da melhor pontua????o
			textoMelhorPontuacao.draw(batch, "SUA MELHOR PONTUA????O ??: "+ pontuacaoMaxima +" PONTOS!",
					larguraDispositivo / 2 - 400, alturaDispositivo / 2 - gameOver.getHeight() / 2 - 150);
		}


//termina a sequencia da aplica????o
		batch.end();
	}



	@Override
	public void dispose () {

	}
}
