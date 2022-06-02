# file-encrypter-decrypter

Programa para encriptación y desencriptación de archivos usando Estándar de Cifrado Avanzado (Advanced Encryption Standard o AES).

## Desarrollo:

> El programa de desarrolló usando Java versión 8 (LTS) con JavaFX. Para las utilidades y operaciones criptográficas, se utilizó el paquete javax.crypto. El programa no permite guardar el hash SHA256 del archivo original en el archivo encriptado porque modificaría el archivo encriptado. Si el archivo encriptado es modificado, el algoritmo AES no puede validar el archivo. No obstante, es posible recuperar el SHA256 del archivo original antes de guardar el archivo encriptado, y posteriormente, compararlo con el SHA256 del archivo desencriptado.

## Cómo usar:

![image](https://user-images.githubusercontent.com/38388199/171580795-695c8a3c-39a2-42e7-a741-fcdb6ab3e828.png)

* Ingrese una contraseña para encriptar su archivo y seleccione `abrir` para escojer un archivo cualquiera en la ubicación que desee.
* El  archivo se encripta y se guarda en la ubicación del archivo original con la extensión `.enc`.

![image](https://user-images.githubusercontent.com/38388199/171581208-c5758da5-c29d-487c-bfbb-c7986714e91a.png)

* Ahora ingrese nuevamente la contraseña con la que encriptó el archivo original y seleccione `abrir` para escojer el archivo encriptado (.enc).
* El archivo desencriptado se guarda en la misma ubicación del archivo encriptado con el prefijo `Decrypted`.

![image](https://user-images.githubusercontent.com/38388199/171581874-7bf6f4c1-3d7d-468b-9d4a-85c7f282e187.png)

* Los hash SHA256 del archivo original y del archivo desencriptado se muestran para validar que son iguales.
* Para volver a encriptar otro archivo, puede seleccionar la pestañana `Archivo` > `Encriptar archivo` con una contraseña.

## Conclusiones:

* El estándar de cifrado avanzado (AES) es un algoritmo de cifrado de clave simétrica ampliamente utilizado que utiliza la misma clave tanto para el cifrado como para el descifrado. El cifrado de bloque de clave simétrica juega un papel importante en el cifrado de datos, por lo que existen múltiples versiones del algoritmo AES.

* Sha256 es una función hash que permite validar si un archivo fue modificado. Aunque no es imposible, es altamente improbable que dos archivos tengan el mismo hash sha256, por lo que resulta un excelente método de validación y seguridad criptográfica.

