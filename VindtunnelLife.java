import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
Organism trying to optimize air-flow through a section hitting a backplate. When the air-particles hit the plate they give off their energy.
the genome consists of 4 decimal numbers a,b,c,d used in the formula: a + b*x^(1/2) + c*ln(d*x+1) + d*x^2 describing the walls of the section.
the upper wall is mirrored to give the lower wall- 

Derivate of formula a + b*x^(1/2) + c*ln(d*x+1) + d*x^2 is used to determine tilt which determines how particles bounce against the walls (using some linear algebra).


**/

class VindtunnelLife extends Lifeform<Double, ArrayList<Double>>{

	final Random RND = new Random();
	final int size = 1000; 
	final double initspeed = 0.01; 
	static JFrame jframe;
	double score=0;
	static Double baseline;
	static long mutationiterations = 0;

	public VindtunnelLife(final ArrayList<Double> genome){
		super(genome);
		if(baseline == null){
			baseline();
			baseline = score;
			score = 0;
		}
	}

	public void start() {
		new HighEndHabitat().start(this.getClass());	
	}

	public ArrayList<Double> mutate(final ArrayList<Double> genome1, final ArrayList<Double> genome2) {
		ArrayList<Double> rtn = new ArrayList<Double>();

		for(int i=0; i<genome1.size(); i++){
			if (RND.nextBoolean()) {
				rtn.add(genome1.get(i));
			} else {
				rtn.add(genome2.get(i));
			}
		}
		return mutate(rtn);
	}
	
	//asexual reproduction
	public ArrayList<Double> mutate(final ArrayList<Double> genome){
		if(RND.nextDouble()<0.15 || mutationiterations<1000) {
			return newGenome();
		}
		mutationiterations++;

		double rate =0.5*RND.nextDouble();

		ArrayList<Double> rtn = new ArrayList<Double>();
		for(int i = 0; i < genome.size(); i++) {
			double a = genome.get(i);
			if(RND.nextBoolean()){
				rtn.add(a*(1-rate) + a*rate*2*RND.nextDouble());
			}
		}

		return rtn;
	}
	
	//calculate the score of own genome
	public double getScore(){
		return score;
	}
	
	//in some manner, output the result of the simulation
	public void output() {
		System.out.println(" "+score+" baseline: "+baseline);
		
		if(jframe == null) {
			jframe = new JFrame();
			jframe.setVisible(true);
			jframe.setSize(size, size);		
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		Graphics g = jframe.getGraphics();

		g.setColor(Color.black);
		g.fillRect(0,0,size,size);

		g.setColor(Color.yellow);

		for(double x =0; x<1; x+=1.0/size){
			double y = functionVal(x);

			g.fillRect((int)(size*x),(int)(size*y),2,2);
			g.fillRect((int)(size*x),(int)(size*(1-y)),2,2);
		}

		double numberofparticles=200;
		for(double i = 0; i < numberofparticles; i++) {
			Particle p = new Particle(0 , i/numberofparticles, 0 );
			g.setColor(new Color(RND.nextInt()));

			for (int iteration=0; iteration < 10000; iteration++) {
				collision(p);
				
				if(outsidebox(p)){
					break;
				}
				g.fillRect((int)(size*p.x),(int)(size*p.y),1,1);
			}
		}
	}
	
	public ArrayList<Double> newGenome(){
		ArrayList<Double> rtn = new ArrayList<Double>();
		rtn.add(RND.nextDouble());//constant
		rtn.add(RND.nextDouble());
		rtn.add(RND.nextDouble());
		rtn.add(RND.nextDouble());//ln
		rtn.add(RND.nextDouble());//quadratic
		return rtn;
	}

	public void run(){
		ArrayList<Particle> particles = new ArrayList<Particle>();
		final double numberofparticles = 200;

		for(double i = 0; i < numberofparticles; i++) particles.add(new Particle(0 , i/numberofparticles, 0 ));

			for (int i=0; i < 10000 && !particles.isEmpty(); i++) {
				particles.stream().forEach(particle -> collision(particle));
				ArrayList tmp = new ArrayList<Double[]>();
				tmp.addAll(particles.stream().filter(particle -> {return !outsideboxScore(particle);}).collect(Collectors.toList()));
				particles=tmp;
			}
			score/=numberofparticles;
		}

		void baseline() {
			ArrayList<Particle> particles = new ArrayList<Particle>();
			final double numberofparticles = 200;

			for(double i = 0; i < numberofparticles; i++) particles.add(new Particle(0 , i/numberofparticles, 0 ));

				for (int i=0; i < 10000 && !particles.isEmpty(); i++) {
					particles.stream().forEach(particle -> {particle.update(); });
					ArrayList tmp = new ArrayList<Double[]>();
					tmp.addAll(particles.stream().filter(particle -> {return !outsideboxScore(particle);}).collect(Collectors.toList()));
					particles=tmp;
				}
				score/=numberofparticles;
			}

			boolean outsideboxScore(Particle particle){
				if(outsidebox(particle)) {
					if(particle.y > 0.4 && particle.y < 0.6 && particle.x > 1){
						score += particle.score();
					}
					return true;
				}
				return false;
			}

			boolean outsidebox(Particle particle){
				return (particle.x<0 || particle.y<0 || particle.x>1 || particle.y>1);
			}

			void collision(Particle particle){
				boolean aboveupperside = aboveUpperSide(particle);
				boolean belowunderside = belowUnderSide(particle);
				particle.update();

				if(aboveUpperSide(particle) != aboveupperside){
					double collisionpointx = halfInterval(particle.x-particle.deltax, particle.deltax, particle.y-particle.deltay, particle.deltay, x->{return functionVal(x);});

					changeDirection(particle, functionDerivateVal(collisionpointx));
					while(aboveUpperSide(particle) != aboveupperside && !outsidebox(particle)) {
						particle.update();	
					}
				} else if (belowUnderSide(particle) != belowunderside) {
					double collisionpointx = halfInterval(particle.x-particle.deltax, particle.deltax, particle.y-particle.deltay, particle.deltay, x->{return 1-functionVal(x);});

			changeDirection(particle, -functionDerivateVal(collisionpointx));//bounce against lower wall yields negative derivate of function
			while(belowUnderSide(particle) != belowunderside && !outsidebox(particle)) {
				particle.update();	
			}
		}
	}

	boolean aboveUpperSide(Particle particle){
		return particle.y < functionVal(particle.x);//function containing outer wall of
	} 

	boolean belowUnderSide(Particle particle){
		return particle.y > 1-functionVal(particle.x);
	}	

	void changeDirection(Particle particle, double derivatey) {
			double normalvectorx = derivatey;//orthagonal to the derivate
			double normalvectory = -1.0;//tilt per length-unit 
			
			double[] parallel = project(particle.deltax, particle.deltay, normalvectorx, normalvectory);

			particle.deltax -= 2*parallel[0];
			particle.deltay -= 2*parallel[1];
			particle.bounceResetSpeed();
		}

	//linear algebra, v1 is projected onto the normal vector 
		double[] project(double v1x, double v1y, double normalx, double normaly){
			double scale = (v1x * normalx + v1y * normaly) / (normalx * normalx + normaly * normaly);
			return new double[]{scale * normalx, scale * normaly};
		}

		double functionVal(double x/*between 0-1*/){
			return genome.get(0) + genome.get(1)*Math.pow(x, 1/2) + genome.get(2)*Math.log(genome.get(3)*x+1) + genome.get(4)*x*x; 
		}

		double functionDerivateVal(double x/*between 0-1*/){
			return (genome.get(1)*Math.pow(x, -1/2))/2 + genome.get(2)*(genome.get(3)/(genome.get(3)*x+1)) + genome.get(4)*2*x;
		}

	// interval halving determining x-coordinate of function at yvalue (to determine collision). 
		double halfInterval(double startx, double xinterval, double yvalue, double yinterval, Function<Double, Double> function) {
			return halfInterval(function.apply(startx) > yvalue, startx, xinterval, yvalue, yinterval, 16 , function);
		}

		double halfInterval(boolean startvalue, double xvalue, double xinterval, double yvalue, double yinterval, int numberofiterations, Function<Double, Double> function) {
			if(numberofiterations == 0) {
				return xvalue+xinterval/2;
			}
			if(function.apply(xvalue) > yvalue == startvalue){
				return halfInterval(startvalue, xvalue+xinterval/2, xinterval/2, yvalue+yinterval/2, yinterval/2, numberofiterations-1, function);
			} else {
				return halfInterval(startvalue, xvalue, xinterval/2, yvalue, yinterval/2, numberofiterations-1, function);
			}
		}

		class Particle{
			double x;
			double y;

			double deltax;
			double deltay;

			public Particle(double x, double y, double angle){
				this.x=x;
				this.y=y;
				deltax = initspeed * Math.cos(angle);
				deltay = initspeed * Math.sin(angle);
			}

			double speed(){
				return Math.hypot(deltax, deltay);
			}

			double score(){
			return deltax*deltax;// x-speed on finishing line
		}

		void update(){
			x += deltax;
			y += deltay;
		}

		void bounceResetSpeed(){
			deltax *= 0.9;
			deltay *= 0.9;
		}
	}
}
