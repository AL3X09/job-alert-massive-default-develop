package co.com.ath.alert.massive.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertMassive {

	private String tipoAlerta;
	private String tipoDocumento;
	private String numeroDocumento;
	private String numeroFactura;
	private String nie;
	private String nura;
	private String aliasMatricula;
	private String valor;
	private String tipoMoneda;
	private Date fechaDia;
	private Date fechaVencimiento;
	private Date fechaLimitePago;
	private String valorMaximo;
	private String tipoMonedaValorMaximo;
	private String tipoCuenta;
	private String numeroCuenta;
	private String causalRechazo;
	private String email;
	private String celular;
	private String codigoBanco; 

}
