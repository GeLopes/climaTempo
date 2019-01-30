	package br.com.teste.controller;
	
	import java.io.IOException;
	import java.text.ParseException;
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.List;
	
	import javax.validation.Valid;
	
	import org.springframework.beans.BeanUtils;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.DeleteMapping;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.PutMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RestController;
	import org.springframework.web.client.RestTemplate;
	
	import com.google.gson.JsonArray;
	import com.google.gson.JsonElement;
	import com.google.gson.JsonObject;
	import com.google.gson.JsonParser;
	
	import br.com.teste.model.Clima;
	import br.com.teste.model.Localizacao;
	import br.com.teste.model.Pessoa;
	import br.com.teste.repository.ClimaRepository;
	import br.com.teste.repository.LocalizacaoRepository;
	import br.com.teste.repository.PessoaRepository;
	
	
	@RestController
	@RequestMapping("/clima")
	public class Controller {
	 
	    @Autowired
		private PessoaRepository pessoaRepository;
	    @Autowired
		private LocalizacaoRepository localizacaoRepository;
	    @Autowired
		private ClimaRepository climaRepository;
		
	  //Método responsável por adicionar Pessoa, localizacao, clima 
		@PostMapping
		public Pessoa adicionar(@Valid @RequestBody Pessoa pessoa) throws IOException, ParseException {
			Clima clima = clima();
			climaRepository.save(clima);
			Localizacao localizacao = localizacao();
			localizacao.setClima(clima);
			localizacaoRepository.save(localizacao);
			pessoa.setLocalizacao(localizacao);
			return pessoaRepository.save(pessoa);
		}
		
		//Método responsável por listar Pessoa, localizacao, clima
		@GetMapping
		public List<Pessoa> listar() {
			return pessoaRepository.findAll();
		}
		
		//Método responsável por consultar Pessoa, localizacao, clima por id
		@GetMapping("/{id}")
		public ResponseEntity<Pessoa> buscar(@PathVariable Long id) {
			Pessoa pessoa = pessoaRepository.findOne(id);
			
			if (pessoa == null) {
				return ResponseEntity.notFound().build();
			}
			
			return ResponseEntity.ok(pessoa);
		}
		
		//Método responsável por atualizar Pessoa, localizacao, clima 
		@PutMapping("/{id}")
		public ResponseEntity<Pessoa> atualizar(@PathVariable Long id, 
				@Valid @RequestBody Pessoa pessoa) {
			Pessoa pessoaExiste = pessoaRepository.findOne(id);
			if (pessoaExiste == null) {
				return ResponseEntity.notFound().build();
				
			}
			Localizacao localizacao = localizacaoRepository.findOne(pessoaExiste.getLocalizacao().getIdLocalizacao());
			Clima clima = climaRepository.findOne(localizacao.getClima().getIdClima());
			
			BeanUtils.copyProperties(pessoa, pessoaExiste, "id");
			localizacao.setClima(clima);
			pessoaExiste.setLocalizacao(localizacao);
			pessoaExiste = pessoaRepository.save(pessoaExiste);
			
			
			return ResponseEntity.ok(pessoaExiste);
		}
	
		//Método responsável por remover Pessoa, localizacao, clima 
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> remover(@PathVariable Long id) {
			Pessoa pessoa = pessoaRepository.findOne(id);
			
			if (pessoa == null) {
				return ResponseEntity.notFound().build();
			}
			
			pessoaRepository.delete(pessoa);
			
			return ResponseEntity.noContent().build();
		}
		
		//Método busca em api aberta através do ip a localização da pessoa que requisitou o post
		public Localizacao localizacao() throws IOException {
			RestTemplate restTemplate = new RestTemplate();
			String loc = restTemplate.getForObject("https://ipvigilante.com/", String.class);
			JsonObject obj = jsonParseObject(loc); 
			JsonObject data = obj.get("data").getAsJsonObject();
			Localizacao localizacao = new Localizacao();
			localizacao.setCidade(data.get("city_name").getAsString());
			localizacao.setLongitude( data.get("longitude").getAsString());
			localizacao.setLatitude(data.get("latitude").getAsString());
			return localizacao;
		}
	
		
	
		//Com a localização encontrada no método: localizacao() Este busca em outra api o woeid
		public String woeid() throws IOException {
			RestTemplate restTemplate = new RestTemplate();
			String result; 
			String distance = null; 
			Long distancia = 0L; 
			Object data = null;
			String woeid = null;
			result = restTemplate.getForObject("https://www.metaweather.com/api/location/search/?query="+localizacao().getCidade(), String.class);
			if (result.length() <= 2) {
				result = restTemplate.getForObject("https://www.metaweather.com/api/location/search/?lattlong="+localizacao().getLatitude()+","+localizacao().getLongitude(), String.class);
				JsonArray array = jsonParseArray(result); 
				for (JsonElement jsonElement : array) {
					data = jsonElement.getAsJsonObject();
					JsonObject obj = jsonParseObject(data.toString());
					distance = obj.get("distance").getAsString();
					if(distancia == 0 || Long.parseLong(distance) < distancia){
						distancia = Long.parseLong(distance);
						woeid = obj.get("woeid").getAsString();
					}
				}
			}else{
				JsonObject obj = jsonParseObject(data.toString());
				woeid = obj.get("woeid").getAsString();
			}
			return woeid;
		}
		 
		//Com o woeid(requisitado para retornar as temperaturas ) da localização este busca a temperatura maxima e minima
		public Clima clima() throws IOException, ParseException {
			 RestTemplate restTemplate = new RestTemplate();
			 SimpleDateFormat formato = new SimpleDateFormat( "yyyy/MM/dd" );
			 String date = formato.format(new Date());
			 String clima = restTemplate.getForObject("https://www.metaweather.com/api/location/"+woeid()+"/"+date, String.class);
			 Object data = null;
			 JsonArray array = jsonParseArray(clima); 
			 Clima cl = null;
			 for (JsonElement jsonElement : array) {
				 cl = new Clima();
				 JsonParser jp = new JsonParser();
				 data = jsonElement.getAsJsonObject();
				 JsonElement elemento = jp.parse(data.toString());
				 JsonObject obj = elemento.getAsJsonObject();
				 cl.setTempMAxima(obj.get("max_temp").getAsString());
				 cl.setTempMinima(obj.get("min_temp").getAsString());
			 }
		 return cl;
		}
		 
		 private JsonObject jsonParseObject(String param) {
				JsonParser jp = new JsonParser();
				JsonElement element = jp.parse(param);
				JsonObject obj = element.getAsJsonObject();
				return obj;
			}
			
		private JsonArray jsonParseArray(String param) {
				JsonParser jp = new JsonParser();
				JsonElement element = jp.parse(param);
				JsonArray array = element.getAsJsonArray(); 
				return array;
			}
		
	}
