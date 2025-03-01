
package EliabRJaraba.imc.modelo;

import java.io.Serializable;

public class calculoImc  implements Serializable{

    private float peso;
    private float altura;
    
   public static class Imc {
        
        public float resultado;
        public String mensaje;
    }
    
   private Imc imc;
    
    public calculoImc (){}
    
    public calculoImc(float peso, float altura){
        this.peso =peso;
        this.altura = altura;
    }
    
    public Imc getImc(){
        imc =new Imc();
        if(peso <= 0 || altura <=0){
            imc.mensaje = "ERROR: El peso y la altura deben ser mayores que 0";
            return imc;
        }else{
            imc.resultado = peso / (altura * altura);
            if(imc.resultado < 18.5){
                imc.mensaje = " Debes consultar de un medico, tu peso es muy bajo ";
            }else if(imc.resultado >= 18.5 && imc.resultado <= 24.9){
                imc.mensaje = "  Estan bien de peso  ";
            }else if(imc.resultado >= 24.9 && imc.resultado <= 29.9){
                imc.mensaje = "  Debes bajar un poco de peso  ";
            }else {
                imc.mensaje = "  Debes consultar de un medico, tu peso es muy alto ";
            }
            return imc;
        }
    }
    
    public float getPeso(){
        return peso;
    }
    
     public void setPeso(float peso){
         this.peso=peso;
    }
    
     public float getAltura(){
        return altura;
    }
    
     public void setAltura(float altura){
         this.altura=altura;
    }
}
