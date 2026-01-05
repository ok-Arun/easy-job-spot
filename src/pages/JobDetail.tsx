import { useParams, Link } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { SEOHead } from '@/components/SEOHead';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { mockJobs } from '@/data/mockJobs';
import { 
  MapPin, Clock, DollarSign, Calendar, Building, ArrowLeft, 
  Share2, Bookmark, CheckCircle, AlertCircle 
} from 'lucide-react';
import { formatDistanceToNow } from 'date-fns';

const JobDetail = () => {
  const { slug } = useParams();
  const job = mockJobs.find(j => j.slug === slug);

  if (!job) {
    return (
      <Layout>
        <SEOHead
          title="Job Not Found"
          description="The job you're looking for doesn't exist or has been removed."
          canonical="/jobs"
        />
        <div className="container mx-auto px-4 py-20 text-center">
          <div className="text-6xl mb-4">🔍</div>
          <h1 className="text-2xl font-bold text-foreground mb-4">Job Not Found</h1>
          <p className="text-muted-foreground mb-6">The job you're looking for doesn't exist or has been removed.</p>
          <Button variant="cta" asChild>
            <Link to="/jobs">Browse All Jobs</Link>
          </Button>
        </div>
      </Layout>
    );
  }

  const formatSalary = () => {
    const { min, max, currency, period } = job.salary;
    return `${currency} ${min} - ${max} per ${period}`;
  };

  // Structured data for SEO
  const structuredData = {
    "@context": "https://schema.org",
    "@type": "JobPosting",
    "title": job.title,
    "description": job.description,
    "hiringOrganization": {
      "@type": "Organization",
      "name": job.company
    },
    "jobLocation": {
      "@type": "Place",
      "address": job.location
    },
    "employmentType": job.type.toUpperCase().replace('-', '_'),
    "datePosted": job.postedAt,
    "validThrough": job.deadline,
    "baseSalary": {
      "@type": "MonetaryAmount",
      "currency": job.salary.currency,
      "value": {
        "@type": "QuantitativeValue",
        "minValue": job.salary.min,
        "maxValue": job.salary.max,
        "unitText": job.salary.period.toUpperCase()
      }
    }
  };

  return (
    <Layout>
      <SEOHead
        title={job.title}
        description={`${job.title} at ${job.company}. ${job.description.slice(0, 140)}...`}
        canonical={`/jobs/${job.slug}`}
        keywords={`${job.title}, ${job.company}, ${job.category}, online job, remote work`}
      />
      <script type="application/ld+json">
        {JSON.stringify(structuredData)}
      </script>

      {/* Breadcrumb */}
      <div className="bg-muted/30 border-b border-border">
        <div className="container mx-auto px-4 py-4">
          <nav className="flex items-center gap-2 text-sm text-muted-foreground" aria-label="Breadcrumb">
            <Link to="/" className="hover:text-foreground transition-colors">Home</Link>
            <span>/</span>
            <Link to="/jobs" className="hover:text-foreground transition-colors">Jobs</Link>
            <span>/</span>
            <Link 
              to={`/jobs?category=${job.category}`} 
              className="hover:text-foreground transition-colors capitalize"
            >
              {job.category.replace('-', ' ')}
            </Link>
            <span>/</span>
            <span className="text-foreground truncate max-w-[200px]">{job.title}</span>
          </nav>
        </div>
      </div>

      {/* Job Header */}
      <section className="bg-muted/30 py-10">
        <div className="container mx-auto px-4">
          <Link 
            to="/jobs" 
            className="inline-flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors mb-6"
          >
            <ArrowLeft className="h-4 w-4" />
            Back to Jobs
          </Link>

          <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-6">
            <div>
              <div className="flex flex-wrap items-center gap-2 mb-4">
                {job.featured && <Badge variant="featured">Featured</Badge>}
                {job.urgent && <Badge variant="urgent">Urgent Hiring</Badge>}
                <Badge variant="category">{job.category.replace('-', ' ')}</Badge>
                <Badge variant="secondary">{job.type}</Badge>
              </div>

              <h1 className="text-3xl md:text-4xl font-bold text-foreground mb-3">
                {job.title}
              </h1>
              
              <div className="flex items-center gap-2 text-lg text-muted-foreground mb-6">
                <Building className="h-5 w-5 text-accent" />
                <span>{job.company}</span>
              </div>

              <div className="flex flex-wrap gap-4 text-sm text-muted-foreground">
                <span className="flex items-center gap-1.5">
                  <MapPin className="h-4 w-4 text-accent" />
                  {job.location}
                </span>
                <span className="flex items-center gap-1.5">
                  <DollarSign className="h-4 w-4 text-accent" />
                  {formatSalary()}
                </span>
                <span className="flex items-center gap-1.5">
                  <Clock className="h-4 w-4 text-accent" />
                  Posted {formatDistanceToNow(new Date(job.postedAt), { addSuffix: true })}
                </span>
                <span className="flex items-center gap-1.5">
                  <Calendar className="h-4 w-4 text-accent" />
                  Apply by {new Date(job.deadline).toLocaleDateString()}
                </span>
              </div>
            </div>

            <div className="flex flex-col sm:flex-row gap-3 lg:shrink-0">
              <Button variant="cta" size="lg" asChild>
                <Link to="/register">Apply Now</Link>
              </Button>
              <Button variant="outline" size="lg">
                <Bookmark className="h-4 w-4" />
                Save Job
              </Button>
              <Button variant="outline" size="lg">
                <Share2 className="h-4 w-4" />
                Share
              </Button>
            </div>
          </div>
        </div>
      </section>

      {/* Job Content */}
      <section className="py-12">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Main Content */}
            <div className="lg:col-span-2 space-y-8">
              {/* Description */}
              <article className="bg-card rounded-xl border border-border p-6 md:p-8">
                <h2 className="text-xl font-semibold text-foreground mb-4">Job Description</h2>
                <p className="text-muted-foreground leading-relaxed">{job.description}</p>
              </article>

              {/* Requirements */}
              <article className="bg-card rounded-xl border border-border p-6 md:p-8">
                <h2 className="text-xl font-semibold text-foreground mb-4">Requirements</h2>
                <ul className="space-y-3">
                  {job.requirements.map((req, index) => (
                    <li key={index} className="flex items-start gap-3">
                      <CheckCircle className="h-5 w-5 text-accent shrink-0 mt-0.5" />
                      <span className="text-muted-foreground">{req}</span>
                    </li>
                  ))}
                </ul>
              </article>

              {/* Benefits */}
              <article className="bg-card rounded-xl border border-border p-6 md:p-8">
                <h2 className="text-xl font-semibold text-foreground mb-4">Benefits</h2>
                <ul className="space-y-3">
                  {job.benefits.map((benefit, index) => (
                    <li key={index} className="flex items-start gap-3">
                      <CheckCircle className="h-5 w-5 text-success shrink-0 mt-0.5" />
                      <span className="text-muted-foreground">{benefit}</span>
                    </li>
                  ))}
                </ul>
              </article>
            </div>

            {/* Sidebar */}
            <aside className="space-y-6">
              {/* Quick Info Card */}
              <div className="bg-card rounded-xl border border-border p-6 sticky top-24">
                <h3 className="font-semibold text-foreground mb-4">Job Overview</h3>
                
                <div className="space-y-4">
                  <div className="flex items-center justify-between py-3 border-b border-border">
                    <span className="text-muted-foreground">Salary</span>
                    <span className="font-medium text-foreground">{formatSalary()}</span>
                  </div>
                  <div className="flex items-center justify-between py-3 border-b border-border">
                    <span className="text-muted-foreground">Job Type</span>
                    <Badge variant="secondary">{job.type}</Badge>
                  </div>
                  <div className="flex items-center justify-between py-3 border-b border-border">
                    <span className="text-muted-foreground">Category</span>
                    <Badge variant="category">{job.category.replace('-', ' ')}</Badge>
                  </div>
                  <div className="flex items-center justify-between py-3 border-b border-border">
                    <span className="text-muted-foreground">Location</span>
                    <span className="font-medium text-foreground text-right text-sm">{job.location}</span>
                  </div>
                  <div className="flex items-center justify-between py-3">
                    <span className="text-muted-foreground">Deadline</span>
                    <span className="font-medium text-foreground">{new Date(job.deadline).toLocaleDateString()}</span>
                  </div>
                </div>

                <Button variant="cta" size="lg" className="w-full mt-6" asChild>
                  <Link to="/register">Apply for This Job</Link>
                </Button>
              </div>

              {/* Skills */}
              <div className="bg-card rounded-xl border border-border p-6">
                <h3 className="font-semibold text-foreground mb-4">Required Skills</h3>
                <div className="flex flex-wrap gap-2">
                  {job.skills.map((skill) => (
                    <Badge key={skill} variant="muted">{skill}</Badge>
                  ))}
                </div>
              </div>

              {/* Warning */}
              <div className="bg-warning/5 border border-warning/20 rounded-xl p-6">
                <div className="flex items-start gap-3">
                  <AlertCircle className="h-5 w-5 text-warning shrink-0 mt-0.5" />
                  <div>
                    <h4 className="font-medium text-foreground mb-1">Safety Tip</h4>
                    <p className="text-sm text-muted-foreground">
                      Never pay to apply for a job. Legitimate employers won't ask for payment during the hiring process.
                    </p>
                  </div>
                </div>
              </div>
            </aside>
          </div>
        </div>
      </section>
    </Layout>
  );
};

export default JobDetail;
