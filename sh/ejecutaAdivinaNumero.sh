export adivinaNumero_HOME=/home/chavezvelajuanignacio/adivinaNumero
export adivinaNumero_CLASS=$adivinaNumero_HOME/src

export CLASSPATH=$adivinaNumero_CLASS

cd $adivinaNumero_CLASS

java cloudComputing.servidor.Servidor >> ../log/adivinaNumero.log
