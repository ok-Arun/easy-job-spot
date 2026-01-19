import { Link } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { SEOHead } from '@/components/SEOHead';
import { SearchBar } from '@/components/SearchBar';
import { JobCard } from '@/components/JobCard';
import { CategoryCard } from '@/components/CategoryCard';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { mockJobs } from '@/data/mockJobs';
import { JOB_CATEGORIES } from '@/types/job';
import { ArrowRight, CheckCircle, Users, Briefcase, TrendingUp, Shield, Clock } from 'lucide-react';

const Index = () => {
  const featuredJobs = mockJobs.filter(job => job.featured).slice(0, 3);
  const recentJobs = mockJobs.slice(0, 4);

  return (
    <Layout>
      <SEOHead
        title="Find Remote Online Jobs"
        description="Discover legitimate online job opportunities in data entry, typing, captcha solving, form filling, and more. Start earning from home today!"
        canonical="/"
        keywords="online jobs, data entry jobs, typing jobs, work from home, remote work, captcha typing, form filling jobs"
      />

      {/* Hero Section */}
      <section className="relative gradient-hero text-primary-foreground overflow-hidden">
        {/* Background Pattern */}
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-20 left-10 w-72 h-72 bg-accent rounded-full blur-3xl" />
          <div className="absolute bottom-10 right-10 w-96 h-96 bg-primary-foreground rounded-full blur-3xl" />
        </div>

        <div className="container mx-auto px-4 py-20 md:py-28 relative">
          <div className="max-w-4xl mx-auto text-center">
            <Badge variant="outline" className="border-primary-foreground/20 text-primary-foreground mb-6 animate-fade-in">
              🚀 Over 10,000+ Active Jobs Available
            </Badge>
            
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold leading-tight mb-6 animate-slide-up">
              Find Your Perfect
              <span className="block text-accent mt-2">Online Job Today</span>
            </h1>
            
            <p className="text-lg md:text-xl text-primary-foreground/80 mb-10 max-w-2xl mx-auto animate-slide-up animation-delay-100">
              Discover legitimate remote work opportunities in data entry, typing, 
              form filling, and more. No experience needed — start earning from home.
            </p>

            {/* Search Bar */}
            <div className="animate-slide-up animation-delay-200">
              <SearchBar />
            </div>

            {/* Popular Searches */}
            <div className="flex flex-wrap justify-center gap-2 mt-8 animate-fade-in animation-delay-300">
              <span className="text-sm text-primary-foreground/60">Popular:</span>
              {['Data Entry', 'Typing Jobs', 'Form Filling', 'Virtual Assistant'].map((term) => (
                <Link
                  key={term}
                  to={`/jobs?search=${encodeURIComponent(term)}`}
                  className="text-sm text-primary-foreground/80 hover:text-accent transition-colors underline-offset-4 hover:underline"
                >
                  {term}
                </Link>
              ))}
            </div>
          </div>
        </div>

        {/* Wave Divider */}
        <div className="absolute bottom-0 left-0 right-0">
          <svg viewBox="0 0 1440 120" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M0 120L60 110C120 100 240 80 360 70C480 60 600 60 720 65C840 70 960 80 1080 85C1200 90 1320 90 1380 90L1440 90V120H1380C1320 120 1200 120 1080 120C960 120 840 120 720 120C600 120 480 120 360 120C240 120 120 120 60 120H0Z"
              className="fill-background"
            />
          </svg>
        </div>
      </section>

      {/* Stats Section */}
      <section className="py-12 -mt-1">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            {[
              { icon: Briefcase, value: '10,000+', label: 'Active Jobs' },
              { icon: Users, value: '50,000+', label: 'Job Seekers' },
              { icon: TrendingUp, value: '85%', label: 'Success Rate' },
              { icon: Clock, value: '24/7', label: 'Support' },
            ].map((stat, index) => (
              <div 
                key={stat.label} 
                className="text-center p-6 rounded-xl bg-card border border-border animate-slide-up"
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <stat.icon className="h-8 w-8 text-accent mx-auto mb-3" />
                <div className="text-2xl md:text-3xl font-bold text-foreground">{stat.value}</div>
                <div className="text-sm text-muted-foreground">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Job Categories */}
      <section className="py-16 md:py-20 bg-muted/30">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
              Browse by Category
            </h2>
            <p className="text-muted-foreground max-w-2xl mx-auto">
              Find the perfect online job that matches your skills and interests
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {JOB_CATEGORIES.map((category, index) => (
              <div 
                key={category.value}
                className="animate-slide-up"
                style={{ animationDelay: `${index * 50}ms` }}
              >
                <CategoryCard 
                  category={category} 
                  jobCount={mockJobs.filter(j => j.category === category.value).length * 15}
                />
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Featured Jobs */}
      <section className="py-16 md:py-20">
        <div className="container mx-auto px-4">
          <div className="flex items-center justify-between mb-12">
            <div>
              <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-2">
                Featured Jobs
              </h2>
              <p className="text-muted-foreground">
                Hand-picked opportunities from top employers
              </p>
            </div>
            <Button variant="outline" asChild className="hidden md:inline-flex">
              <Link to="/jobs">
                View All Jobs
                <ArrowRight className="h-4 w-4" />
              </Link>
            </Button>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {featuredJobs.map((job, index) => (
              <div 
                key={job.id}
                className="animate-slide-up"
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <JobCard job={job} />
              </div>
            ))}
          </div>

          <div className="text-center mt-10 md:hidden">
            <Button variant="outline" asChild>
              <Link to="/jobs">
                View All Jobs
                <ArrowRight className="h-4 w-4" />
              </Link>
            </Button>
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="py-16 md:py-20 bg-muted/30">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
              How It Works
            </h2>
            <p className="text-muted-foreground max-w-2xl mx-auto">
              Start earning online in just 3 simple steps
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {[
              {
                step: '01',
                title: 'Create Your Account',
                description: 'Sign up for free and complete your profile with your skills and experience.',
                icon: Users,
              },
              {
                step: '02',
                title: 'Browse & Apply',
                description: 'Search through thousands of jobs and apply to the ones that match your skills.',
                icon: Briefcase,
              },
              {
                step: '03',
                title: 'Start Earning',
                description: 'Get hired and start working from home. Receive payments securely.',
                icon: TrendingUp,
              },
            ].map((item, index) => (
              <div 
                key={item.step}
                className="relative text-center p-8 rounded-xl bg-card border border-border animate-slide-up"
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <div className="absolute -top-4 left-1/2 -translate-x-1/2 bg-accent text-accent-foreground text-sm font-bold px-4 py-1 rounded-full">
                  Step {item.step}
                </div>
                <div className="h-16 w-16 rounded-2xl bg-accent/10 flex items-center justify-center mx-auto mt-4 mb-6">
                  <item.icon className="h-8 w-8 text-accent" />
                </div>
                <h3 className="text-xl font-semibold text-foreground mb-3">{item.title}</h3>
                <p className="text-muted-foreground">{item.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Recent Jobs */}
      <section className="py-16 md:py-20">
        <div className="container mx-auto px-4">
          <div className="flex items-center justify-between mb-12">
            <div>
              <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-2">
                Latest Job Listings
              </h2>
              <p className="text-muted-foreground">
                Fresh opportunities added daily
              </p>
            </div>
            <Button variant="outline" asChild className="hidden md:inline-flex">
              <Link to="/jobs">
                Browse All
                <ArrowRight className="h-4 w-4" />
              </Link>
            </Button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {recentJobs.map((job, index) => (
              <div 
                key={job.id}
                className="animate-slide-up"
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <JobCard job={job} />
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Why Choose Us */}
      <section className="py-16 md:py-20 bg-muted/30">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-6">
                Why Choose OnlineJobsHub?
              </h2>
              <p className="text-muted-foreground mb-8">
                We're committed to connecting you with legitimate, high-quality remote work opportunities. 
                Our platform is trusted by thousands of job seekers worldwide.
              </p>

              <div className="space-y-4">
                {[
                  'Verified and legitimate job postings only',
                  'No experience required for many positions',
                  'Flexible working hours — work when you want',
                  'Secure payment protection',
                  '24/7 customer support',
                  'Free to join and apply',
                ].map((feature) => (
                  <div key={feature} className="flex items-center gap-3">
                    <CheckCircle className="h-5 w-5 text-accent shrink-0" />
                    <span className="text-foreground">{feature}</span>
                  </div>
                ))}
              </div>

              <div className="flex gap-4 mt-8">
                <Button variant="cta" size="lg" asChild>
                  <Link to="/register">Get Started Free</Link>
                </Button>
                <Button variant="outline" size="lg" asChild>
                  <Link to="/about">Learn More</Link>
                </Button>
              </div>
            </div>

            <div className="relative">
              <div className="bg-gradient-to-br from-accent/20 to-primary/20 rounded-3xl p-8 md:p-12">
                <div className="grid grid-cols-2 gap-6">
                  {[
                    { icon: Shield, label: 'Secure Platform', value: '100%' },
                    { icon: Users, label: 'Happy Users', value: '50K+' },
                    { icon: Briefcase, label: 'Jobs Filled', value: '25K+' },
                    { icon: Clock, label: 'Avg. Response', value: '24hrs' },
                  ].map((stat) => (
                    <div key={stat.label} className="bg-card rounded-xl p-6 text-center shadow-elegant">
                      <stat.icon className="h-8 w-8 text-accent mx-auto mb-3" />
                      <div className="text-2xl font-bold text-foreground">{stat.value}</div>
                      <div className="text-sm text-muted-foreground">{stat.label}</div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 md:py-20">
        <div className="container mx-auto px-4">
          <div className="gradient-hero rounded-3xl p-8 md:p-16 text-center text-primary-foreground relative overflow-hidden">
            <div className="absolute inset-0 opacity-10">
              <div className="absolute top-10 right-10 w-64 h-64 bg-accent rounded-full blur-3xl" />
            </div>
            <div className="relative">
              <h2 className="text-3xl md:text-4xl font-bold mb-4">
                Ready to Start Earning Online?
              </h2>
              <p className="text-lg text-primary-foreground/80 mb-8 max-w-2xl mx-auto">
                Join thousands of people who have found their perfect online job through our platform.
              </p>
              <div className="flex flex-col sm:flex-row gap-4 justify-center">
                <Button variant="cta" size="xl" asChild>
                  <Link to="/register">Create Free Account</Link>
                </Button>
                <Button variant="hero" size="xl" asChild>
                  <Link to="/jobs">Browse Jobs</Link>
                </Button>
              </div>
            </div>
          </div>
        </div>
      </section>
    </Layout>
  );
};

export default Index;
