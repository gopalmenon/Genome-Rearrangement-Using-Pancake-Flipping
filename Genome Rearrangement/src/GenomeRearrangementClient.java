
public class GenomeRearrangementClient {

	public static void main(String[] args) {
		
		GenomeRearrangementClient genomeRearrangementClient = new GenomeRearrangementClient();
		genomeRearrangementClient.restoreToIdentitySequence();
	}
	
	public void restoreToIdentitySequence() {
		
		GenomeRearrangement genomeRearrangement = new GenomeRearrangement(10);
		System.out.println(genomeRearrangement.getGenomicSequence());
		System.out.println("Number of flipes was " + genomeRearrangement.getNumberOfFlipsToGenerateGenomicSequence());
		
	}
}
