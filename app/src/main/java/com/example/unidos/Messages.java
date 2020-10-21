package com.example.unidos;
//changeq
public class Messages {
    public String findMessage(String msg){
        switch (msg) {
            case "notFound" :
                return "Lo sentimos, ha surgido un problema al realizar la operación. Por favor, revisa tu conexión a internet e inténtalo de nuevo.";

            case "noPhoneMatch" :
                return "Ingresar únicamente [0-9].";

            case "noCURPmatch" :
                return "No cumple con el formato de una CURP.";

            case "empty" :
                return "Ingresa un valor.";

            case "noNameMatch" :
                return "Ingresa únicamente:(Aa-Zz), acentos y espacios.";

            case "fail" :
                return "Lo sentimos, ha surgido un problema al realizar la operación. Por favor, revisa tu conexión a internet e inténtalo de nuevo.";

            case "badConn" :
                return "Al parecer no te encuentras conectado a internet o la conexión puede estar presentando problemas que afectan la comunicación con los servicios de la aplicación, por favor, revisa tu conexión y vuelve a intentarlo.";

            case "noConn" :
                return "Al parecer no se encuentra habilitado tu Wi-Fi ni tus datos celulares. Es necesario contar con conexión a internet para poder utilizar la aplicación.";

            case  "noUsr":
                return "Al parecer no te encuentras registrado en la aplicación.";

            case "phoneVerifFail" :
                return "Lo sentimos, ha ocurrido un problema durante la verificación de tu número telefónico. Favor de revisar que el número ingresado sea correcto.";

            case "usrFound" :
                return "La CURP ingresada ya se encuentra resgistrada.";

            case "opSuccess" :
                return "La información se ha registrado exitosamente.";

            case "phoneFound" :
                return "El número telefónico ya se encuentra registrado.";

            case "deleted":
                return "La cuenta se ha dado de baja.";

            case "failDelete":
                return "Ha ocurrido un problema al intentar dar de baja la cuenta";
            case "update":
                return "La información se ha actualizado.";
            case "noChange":
                return "No se han detectado cambios en la información.";
            default:
                return "";
        }
    }
}
   