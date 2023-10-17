# Servidor Webhook

Servidor em Java puro para receber callbacks dos webhooks cadastrados nas APIs de cobrança, pix e banking.
Valida a origem da chamada com a chave pública ca.crt disponibilizada.

## Pré-requisitos

### Java
Versão 8 ou superior.

### OpenSSL
Para facilitar a instalação, recomendamos a instalação do [Git](https://git-scm.com/downloads), pois com ele é instalado automaticamente o OpenSSL.
Caso deseje instalar somenteo o openSSL, clique [aqui](https://www.openssl.org/source/) e siga as instruções de acordo com o seu sistema operacional.

### Maven
O projeto utiliza o [maven](https://maven.apache.org) como gerenciador de dependências e automação de compilação.


## Domínio
Será necessário um domínio privado para a configuração do certificado. Caso não possua, deverá ser criado.
Abaixo, utilizaremos como exemplo o domínio fictício **https://meu-webhook-inter.com.br**.

### Certificado de Domínio
Com o domínio já criado, será necessária a configuração de um certificado TLS.
Como esse certificado pode ser gerado de diversas formas, usaremos nesse exemplo um certificado confiável gerado através do site [zeroSSL](https://zerossl.com).
(Na versão gratuita os certificados possuem apenas 90 dias de validade!)

É importante ressaltar que esse certificado `não pode ser autoassinado` e deve ser gerado através de uma `CA confiável`.

Após o download do .zip, teremos em mãos 3 arquivos:
- **certificate.crt** : Seu certificado em si
- **private.key** : Chave privada do seu certificado (Esse arquivo não deve ser compartilhado!)
- **ca_bundle.crt** : Conjunto dos certificados intermediários e raiz


Executamos o seguinte comando para que seja gerado um único arquivo (certchain.crt).

`Esse passo é muito importante para que o seu servidor seja reconhecido como confiável pelos clientes que tentarem acessá-lo (no caso o Inter)`.
```
cat certificate.crt ca_bundle.crt > certchain.crt
```

### Exportando o certificado no formato PKCS12
No caso dessa configuração, trabalharemos com o formato PKCS12 e, para isso precisamos utilizar comando abaixo:
```
openssl pkcs12 -export -out server.p12 -inkey private.key -in certchain.crt -passout pass:123456
```
Será gerado o arquivo **server.p12**
Observe que o password utilizado nesse caso foi 123456. Caso deseje, esse valor pode ser alterado.


### Importando a CA na truststore

Esse passo serve para que o seu servidor passe a confiar nas requisições realizadas pelo Inter.
****
**Atenção:**

Caso deseje desabilitar esse comportamento, e não exigir uma autenticação no envio do callback, você deve alterar o método `initChannel` da classe `HttpServerInitializer`.
```java
//Alterar de 'true' para false
sslEngine.setNeedClientAuth(false);
```
Dessa forma qualquer requisição realizada para o seu endpoint será aceita, independente da origem. Sendo assim, `não recomendamos essa configuração!`
****

Após o donwload do certificado webhook [Como configurar](https://developers.inter.co/docs/webhooks/como-config-webhooks), descompacte o arquivo baixado.

Na pasta aonde o arquivo ca.crt execute o comando:
```
keytool -import -trustcacerts -noprompt -alias ca -file ca.crt -keystore truststore.jks
```
Observe que utilizamos fornecemos aqui também uma senha, e utilizamos como exemplo 123456

Será gerado o arquivo **truststore.jks**

Com os 2 arquivos em mãos (server.p12 e truststore.jks), devemos colocá-los na pasta /resource/certs, do código disponibilizado.

### Configuração dos passwords da aplicação

Atualize o arquivo "Config.json" com as senhas utilizadas durante o processo.

```json
{
  "TRUSTSTORE_PASSWORD": "123456",
  "KEYSTORE_PASSWORD": "123456"
}
```

### Compilando o projeto

Utilizando o Maven, execute o comando **mvn clean install** para que seja compilado seu código.
Será gerada na pasta target, dentro do projeto, o seguinte arquivo:

**`servidor-webhook-java-1.0.0-SNAPSHOT-jar-with-dependencies.jar`**

Esse arquivo é o seu jar executável. Coloque-o no seu servidor e execute-o.

Ex: 
No terminal, digite o comando:
```
nohup java -jar servidor-webhook-java-1.0.0-SNAPSHOT-jar-with-dependencies.jar &
```
O '&' no final do comando serve para que o serviço não bloqueie o terminal. Assim ele é executado em background.

Para visualizar os logs da aplicação em tempo real, digite:
```
tail -f nohup.out
```
Dessa forma ao receber um callback ou caso tenha algum erro no recebimento ou configuração do servidor, as informações serão exibidas na tela instantaneamente.


Lembre-se que o exemplo disponibilizado utiliza a porta **8443**. Essa porta pode ser modificada no código e devemos atentar também a liberação de acesso a porta via https no servidor!

Os endpoints a serem configurados como webhooks seriam:

- **COBRANÇA**: https://meu-webhook-inter.com.br:8443/cobranca
- **PIX**: https://meu-webhook-inter.com.br:8443/pix
- **PIX-PAGAMENTO**: https://meu-webhook-inter.com.br:8443/banking


## Observações

Esse é um projeto simples que recebe as chamadas callback com autenticação da origem. No diretório
src/main/java/inter/uses estão os fluxos com os dados recebidos.
Esses fluxos devem ser tratados de acordo com a necessidade do cliente. No momento, eles apenas imprimem a conta
corrente recebida e o conteúdo do callback.

Questões como padronização do tratamento de excessões e logs não foram tratados para que os clientes possam definir a
sua maneira. Pode-se completar esse projeto para ser utilizado como servidor ou utiliza-lo como exemplo para criar o seu
próprio.


## Troubleshooting

Seguem alguns comando importantes a serem utilizados no servidor.

- Caso a aplicação esteja rodando e seja preciso terminá-la, utilize o comando:
```
ps -ef | grep java
```
para visualizar os processos que estejam rodando.
Ao visualizar o processo que aponta para o .jar, copie o ID do processo e insira o comando:
```
kill ID_PROCESSO
```
Aonde o ID_PROCESSO deve ser substituído pelo ID obtido no passo anterior.

Com isso a execução do .jar será encerrada.


