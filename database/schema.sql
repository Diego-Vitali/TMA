CREATE DATABASE TMA;

use TMA;

create table tb_usuarios(
	id int primary key unique auto_increment,
	nome varchar(55),
	email varchar (55),
	senha varchar(55),
	tipo varchar(20),
	status varchar(15)
);

create table tb_previsao(
	id int primary key unique auto_increment,
	data_criacao timestamp,
	id_usuario int,
	versao_modelo int,
	transit_time_output int,
	peso_bruto_input float,
	metro_cubico_input float,
	valor_mercadoria_input float,
	quantidade_volumes_input int,
	via_transporte_input varchar(55),
	uf_origem_input varchar(2),
	cep_origem_input varchar(9),
	uf_destino_input varchar(2),
	cep_destino_input varchar(9),
	FOREIGN KEY (id_usuario) REFERENCES tb_usuarios(id)
);

create table tb_entregas(
	id int primary key unique auto_increment,
	data_criacao timestamp,
	id_usuario int,
	versao_modelo int,
	transit_time int,
	peso_bruto float,
	metro_cubico float,
	valor_mercadoria float,
	quantidade_volumes int,
	via_transporte varchar(55),
	uf_origem varchar(2),
	cep_origem varchar(9),
	uf_destino varchar(2),
	cep_destino varchar(9),
	FOREIGN KEY (id_usuario) REFERENCES tb_usuarios(id)
);

create table tb_modelo(
	versao int primary key,
	tipo varchar(55),
	data_ultimo_treinamento timestamp,
	id_usuario_ultimo_treinamento int,
	status varchar(15),
	FOREIGN KEY (id_usuario_ultimo_treinamento) REFERENCES tb_usuarios(id)
);