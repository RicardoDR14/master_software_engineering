all: JOGOUI motor bot

JOGOUI: JOGOUI.C
	gcc -o JOGOUI JOGOUI.C -lncurses

motor: motor.c 
	gcc -o motor motor.c -pthread
	
bot: bot.c
	gcc -o bot bot.c 
	
clean:
	rm -f JOGOUI motor bot cliente_servidor servidor_cliente
	








