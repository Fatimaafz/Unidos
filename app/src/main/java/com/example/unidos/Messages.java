package com.example.unidos;

public class Messages {
    public static final String MSG001_1_OP_SUCCESS = "La operación se ha realizado exitosamente.";
    public static final String MSG001_2_WELCOME="Bienvenido";
    public static final String MSG002_1_USER_DUPLICATED="La CURP ingresada ya se encuentra resgistrada.";
    public static final String MSG002_2_PHONE_DUPLICATED ="El número telefónico ya se encuentra registrado.";
    public static final String MSG002_3_NO_INFO_CHANGE = "No se han detectado cambios en la información.";
    public static final String MSG003_1_OP_FAIL="Lo sentimos, ha surgido un problema al realizar la operación. Por favor, revisa tu conexión a internet e inténtalo de nuevo.";
    public static final String MSG003_1_NO_INFO_TO_SHOW="No hay información para mostrar.";
    public static final String MSG003_1_USER_NOT_FOUND="Al parecer no te encuentras registrado en la aplicación.";
    public static final String MSG004_1_NO_INTERNET="Hemos detectado problemas con tu conexión a internet. Es necesario contar con conexión a internet para poder utilizar la aplicación.";
    public static final String MSG005_1_AUTOVERIF_FAILED="Lo sentimos, ha ocurrido un problema durante la verificación de tu número telefónico. Favor de revisar que el número ingresado sea correcto.";
    public static final String MSG006_1_EMPTY_FIELD="Ingresa un valor.";
    public static final String MSG006_2_WRONG_CURP_SYNTAX="No cumple con el formato de una CURP.";
    public static final String MSG006_2_1_WRONG_PHONE_SYNTAX="Ingresar únicamente [0-9].";
    public static final String MSG006_2_2_WRONG_NAME_SYNTAX="Ingresa únicamente:(Aa-Zz), acentos y espacios.";
    public static final String MSG007_1_ENABLE_GPS="Es necesario que actives tu ubicación";
    public static final String MSG007_2_LOCATION_FAIL="Lo sentimos, ha surgido un problema al determinar tu ubicación. Verifica que tu GPS se encuentre activado.";
    public static final String MSG007_3_SAME_LOCATION="Al parecer te encuentras en la misma ubicación o a menos de 500 m. de la ubciación anterior. ";
    public static final String MSG008_1_CONFIRMATION="¿Estás seguro de que deseas darte de baja de la aplicación?";
    public static final String MSG008_2_WARNING="1.\tLos datos proporcionados únicamente se utilizarán con la finalidad de dar seguimiento a los reportes y como medio de contacto con los autores. \n\n" +
            "2.\tLa información de todos los usuarios se encuentra protegida por los derechos ARCO y Ley Federal de Protección de Datos Personales en Posesión de los Particulares. \n\n" +
            "3.\tAl formar parte de Unidos te comprometerás a utilizar responsablemente la aplicación, pues la información disponible es de carácter sensible y la localización de las personas puede verse afectada por los reportes que se generen.\n\n";




    public static final String ERR_01_ID = "ERR_MSSG_01";
    public static final String ERR_MSSG_00 = "Hemos detectado problemas con tu conexión a internet. Es necesario contar con conexión a internet para poder utilizar la aplicación.";
    public static final String ERR_MSSG_01 = "Lo sentimos, ha surgido un problema al realizar la operación. Por favor, revisa tu conexión a internet e inténtalo de nuevo.";
    public static final String ERR_MSSG_01_1 = "Lo sentimos, ha surgido un problema al intentar obtener cierta información.";
    public static final String ERR_MSSG_01_2 = "No hay información para mostrar.";
    public static final String ERR_MSSG_02 = "No se encontraron coincidencias";
    public static final String ERR_MSSG_03 = "Al parecer no se encuentra habilitado tu Wi-Fi ni tus datos celulares. Es necesario contar con conexión a internet para poder utilizar la aplicación.";
    public static final String ERR_MSSG_04 = "Al parecer no te encuentras conectado a internet o la conexión puede estar presentando problemas que afectan la comunicación con los servicios de la aplicación, por favor, revisa tu conexión y vuelve a intentarlo.";
    public static final String FIELD_ERR_01 = "Ingresa un valor.";
    public static final String OP_SUCCESS_MSSG = "La información se ha registrado exitosamente.";
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
   