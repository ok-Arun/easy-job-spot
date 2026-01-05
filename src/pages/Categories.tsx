import { Link } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { SEOHead } from '@/components/SEOHead';
import { CategoryCard } from '@/components/CategoryCard';
import { JOB_CATEGORIES } from '@/types/job';
import { mockJobs } from '@/data/mockJobs';
import { ArrowRight } from 'lucide-react';
import { Button } from '@/components/ui/button';

const Categories = () => {
  return (
    <Layout>
      <SEOHead
        title="Job Categories"
        description="Browse online job opportunities by category. Find data entry, typing, form filling, captcha solving, and more remote work categories."
        canonical="/categories"
        keywords="job categories, online work categories, data entry jobs, typing jobs, remote work types"
      />

      {/* Header */}
      <section className="bg-muted/30 py-16">
        <div className="container mx-auto px-4">
          <nav className="flex items-center gap-2 text-sm text-muted-foreground mb-6" aria-label="Breadcrumb">
            <Link to="/" className="hover:text-foreground transition-colors">Home</Link>
            <span>/</span>
            <span className="text-foreground">Categories</span>
          </nav>

          <h1 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
            Browse by Category
          </h1>
          <p className="text-muted-foreground max-w-2xl">
            Explore our comprehensive list of online job categories. Find the perfect remote work 
            opportunity that matches your skills and interests.
          </p>
        </div>
      </section>

      {/* Categories Grid */}
      <section className="py-16">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {JOB_CATEGORIES.map((category, index) => {
              const jobCount = mockJobs.filter(j => j.category === category.value).length * 15;
              return (
                <div 
                  key={category.value}
                  className="animate-slide-up"
                  style={{ animationDelay: `${index * 50}ms` }}
                >
                  <CategoryCard category={category} jobCount={jobCount} />
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 bg-muted/30">
        <div className="container mx-auto px-4 text-center">
          <h2 className="text-2xl md:text-3xl font-bold text-foreground mb-4">
            Can't Find Your Category?
          </h2>
          <p className="text-muted-foreground mb-8 max-w-xl mx-auto">
            We're constantly adding new job categories. Sign up to get notified when new opportunities become available.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button variant="cta" size="lg" asChild>
              <Link to="/register">
                Create Account
                <ArrowRight className="h-4 w-4" />
              </Link>
            </Button>
            <Button variant="outline" size="lg" asChild>
              <Link to="/jobs">Browse All Jobs</Link>
            </Button>
          </div>
        </div>
      </section>
    </Layout>
  );
};

export default Categories;
