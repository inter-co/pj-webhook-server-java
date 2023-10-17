# Servidor Webhook

Servidor em Java puro para receber callbacks dos webhooks cadastrados nas APIs de cobrança, pix e banking.
Valida a origem da chamada com a chave pública ca.crt disponibilizada.

## Pré-requisitos

- Java 8 ou superior

## Download e configuração da chave pública

Para baixar a chave pública, siga as instruções desse
documento: <https://developers.bancointer.com.br/docs/como-configurar-um-webhook>

Verifique se possui o openssl instalado em sua maquina com o seguinte comando no terminal: openssl version.
Caso não tenha instalado, instale o openssl. Ele será necessário para gerar o arquivo da chave pública no formato
utilizado pelo Java nesse servidor (.jks).

* Baixar OpenSSL para MAC: <https://www.macupdate.com/app/mac/62162/openssl>
* Baixar OpenSSL para Windows: <https://slproweb.com/products/Win32OpenSSL.html>
* Baixar OpenSSL para
  Linux: <https://help.dreamhost.com/hc/en-us/articles/360001435926-Installing-OpenSSL-locally-under-your-username>

Gere o arquivo .jks com o seguinte comando:

```
keytool -import -trustcacerts -noprompt -alias ca \
        -ext san=dns:localhost,ip:127.0.0.1 \
        -file ca.crt -keystore truststore.jks
```

Será requisitado uma senha para o repositório. Essa senha deverá ser colocada no arquivo Config.json, localizado na
pasta config do projeto.
O arquivo .jks gerado deverá ser colocado na pasta certs do projeto com o nome truststore.jks.
Coloque um certificado autenticavel, não pode ser autoassinado, para identificar o servidor na pasta certs do projeto.
Ele deve ser do tipo .p12. O nomeie como server.p12. A senha desse certificado deverá ser colocada no arquivo
Config.json, localizado na pasta config do projeto.

## Execução

Esse é um projeto simples que recebe as chamadas callback com autenticação da origem. No diretório
src/main/java/inter/uses estão os fluxos com os dados recebidos.
Esses fluxos devem ser tratados de acordo com a necessidade do cliente. No momento, eles apenas imprimem a conta
corrente recebida do callback.

Questões como padronização do tratamento de excessões e logs não foram tratados para que os clientes possam definir a
sua maneira. Pode-se completar esse projeto para ser utilizado como servidor ou utiliza-lo como exemplo para criar o seu
próprio.
