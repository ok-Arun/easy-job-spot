import { useState, useMemo } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { SEOHead } from '@/components/SEOHead';
import { SearchBar } from '@/components/SearchBar';
import { JobCard } from '@/components/JobCard';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { mockJobs } from '@/data/mockJobs';
import { JOB_CATEGORIES, JobCategory } from '@/types/job';
import { Filter, X, ChevronDown } from 'lucide-react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

const Jobs = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [showFilters, setShowFilters] = useState(false);

  const category = searchParams.get('category') as JobCategory | null;
  const search = searchParams.get('search') || '';
  const type = searchParams.get('type') || '';
  const sort = searchParams.get('sort') || 'newest';

  const filteredJobs = useMemo(() => {
    let jobs = [...mockJobs];

    // Filter by category
    if (category) {
      jobs = jobs.filter(job => job.category === category);
    }

    // Filter by search
    if (search) {
      const searchLower = search.toLowerCase();
      jobs = jobs.filter(job =>
        job.title.toLowerCase().includes(searchLower) ||
        job.company.toLowerCase().includes(searchLower) ||
        job.description.toLowerCase().includes(searchLower)
      );
    }

    // Filter by type
    if (type) {
      jobs = jobs.filter(job => job.type === type);
    }

    // Sort
    switch (sort) {
      case 'newest':
        jobs.sort((a, b) => new Date(b.postedAt).getTime() - new Date(a.postedAt).getTime());
        break;
      case 'salary-high':
        jobs.sort((a, b) => b.salary.max - a.salary.max);
        break;
      case 'salary-low':
        jobs.sort((a, b) => a.salary.min - b.salary.min);
        break;
    }

    return jobs;
  }, [category, search, type, sort]);

  const updateFilter = (key: string, value: string) => {
    const newParams = new URLSearchParams(searchParams);
    if (value) {
      newParams.set(key, value);
    } else {
      newParams.delete(key);
    }
    setSearchParams(newParams);
  };

  const clearFilters = () => {
    setSearchParams({});
  };

  const activeFiltersCount = [category, search, type].filter(Boolean).length;

  return (
    <Layout>
      <SEOHead
        title="Browse Online Jobs"
        description="Find the best online jobs in data entry, typing, captcha solving, form filling, and more. Apply today and start earning from home."
        canonical="/jobs"
        keywords="online jobs, remote jobs, data entry, typing jobs, work from home jobs"
      />

      {/* Header */}
      <section className="bg-muted/30 py-12">
        <div className="container mx-auto px-4">
          <nav className="flex items-center gap-2 text-sm text-muted-foreground mb-6" aria-label="Breadcrumb">
            <Link to="/" className="hover:text-foreground transition-colors">Home</Link>
            <span>/</span>
            <span className="text-foreground">Jobs</span>
            {category && (
              <>
                <span>/</span>
                <span className="text-foreground capitalize">{category.replace('-', ' ')}</span>
              </>
            )}
          </nav>

          <h1 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
            {category 
              ? `${JOB_CATEGORIES.find(c => c.value === category)?.label} Jobs`
              : 'Browse All Online Jobs'
            }
          </h1>
          <p className="text-muted-foreground mb-8 max-w-2xl">
            {filteredJobs.length} jobs available matching your criteria
          </p>

          <SearchBar />
        </div>
      </section>

      {/* Main Content */}
      <section className="py-12">
        <div className="container mx-auto px-4">
          <div className="flex flex-col lg:flex-row gap-8">
            {/* Filters Sidebar */}
            <aside className={`lg:w-72 shrink-0 ${showFilters ? 'block' : 'hidden lg:block'}`}>
              <div className="bg-card rounded-xl border border-border p-6 sticky top-24">
                <div className="flex items-center justify-between mb-6">
                  <h2 className="font-semibold text-foreground">Filters</h2>
                  {activeFiltersCount > 0 && (
                    <Button variant="ghost" size="sm" onClick={clearFilters}>
                      Clear all
                    </Button>
                  )}
                </div>

                {/* Category Filter */}
                <div className="mb-6">
                  <label className="text-sm font-medium text-foreground mb-3 block">Category</label>
                  <Select value={category || 'all'} onValueChange={(value) => updateFilter('category', value === 'all' ? '' : value)}>
                    <SelectTrigger>
                      <SelectValue placeholder="All Categories" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Categories</SelectItem>
                      {JOB_CATEGORIES.map((cat) => (
                        <SelectItem key={cat.value} value={cat.value}>
                          {cat.icon} {cat.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>

                {/* Job Type Filter */}
                <div className="mb-6">
                  <label className="text-sm font-medium text-foreground mb-3 block">Job Type</label>
                  <Select value={type || 'all'} onValueChange={(value) => updateFilter('type', value === 'all' ? '' : value)}>
                    <SelectTrigger>
                      <SelectValue placeholder="All Types" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Types</SelectItem>
                      <SelectItem value="Full-time">Full-time</SelectItem>
                      <SelectItem value="Part-time">Part-time</SelectItem>
                      <SelectItem value="Contract">Contract</SelectItem>
                      <SelectItem value="Freelance">Freelance</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                {/* Active Filters */}
                {activeFiltersCount > 0 && (
                  <div className="pt-4 border-t border-border">
                    <p className="text-sm text-muted-foreground mb-3">Active Filters:</p>
                    <div className="flex flex-wrap gap-2">
                      {category && (
                        <Badge variant="accent" className="gap-1">
                          {category.replace('-', ' ')}
                          <button onClick={() => updateFilter('category', '')} aria-label="Remove category filter">
                            <X className="h-3 w-3" />
                          </button>
                        </Badge>
                      )}
                      {type && (
                        <Badge variant="accent" className="gap-1">
                          {type}
                          <button onClick={() => updateFilter('type', '')} aria-label="Remove type filter">
                            <X className="h-3 w-3" />
                          </button>
                        </Badge>
                      )}
                      {search && (
                        <Badge variant="accent" className="gap-1">
                          "{search}"
                          <button onClick={() => updateFilter('search', '')} aria-label="Remove search filter">
                            <X className="h-3 w-3" />
                          </button>
                        </Badge>
                      )}
                    </div>
                  </div>
                )}
              </div>
            </aside>

            {/* Jobs List */}
            <div className="flex-1">
              {/* Sort & Mobile Filter Toggle */}
              <div className="flex items-center justify-between mb-6">
                <Button
                  variant="outline"
                  className="lg:hidden"
                  onClick={() => setShowFilters(!showFilters)}
                >
                  <Filter className="h-4 w-4" />
                  Filters
                  {activeFiltersCount > 0 && (
                    <Badge variant="accent" className="ml-2">{activeFiltersCount}</Badge>
                  )}
                </Button>

                <div className="flex items-center gap-2 ml-auto">
                  <span className="text-sm text-muted-foreground">Sort by:</span>
                  <Select value={sort} onValueChange={(value) => updateFilter('sort', value)}>
                    <SelectTrigger className="w-40">
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="newest">Newest First</SelectItem>
                      <SelectItem value="salary-high">Highest Salary</SelectItem>
                      <SelectItem value="salary-low">Lowest Salary</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>

              {/* Jobs Grid */}
              {filteredJobs.length > 0 ? (
                <div className="space-y-6">
                  {filteredJobs.map((job, index) => (
                    <div
                      key={job.id}
                      className="animate-slide-up"
                      style={{ animationDelay: `${index * 50}ms` }}
                    >
                      <JobCard job={job} />
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-16">
                  <div className="text-6xl mb-4">🔍</div>
                  <h3 className="text-xl font-semibold text-foreground mb-2">No jobs found</h3>
                  <p className="text-muted-foreground mb-6">
                    Try adjusting your search filters or browse all categories
                  </p>
                  <Button variant="cta" onClick={clearFilters}>
                    Clear All Filters
                  </Button>
                </div>
              )}
            </div>
          </div>
        </div>
      </section>
    </Layout>
  );
};

export default Jobs;
