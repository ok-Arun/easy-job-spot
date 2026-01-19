import { useState, useMemo, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { SEOHead } from '@/components/SEOHead';
import { SearchBar } from '@/components/SearchBar';
import { JobCard } from '@/components/JobCard';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { JOB_CATEGORIES, JobCategory } from '@/types/job';
import { Filter, X } from 'lucide-react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

const Jobs = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [showFilters, setShowFilters] = useState(false);

  const [jobs, setJobs] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const category = searchParams.get('category') as JobCategory | null;
  const search = searchParams.get('search') || '';
  const type = searchParams.get('type') || '';
  const sort = searchParams.get('sort') || 'newest';

  // =========================
  // Fetch jobs from backend
  // =========================
  useEffect(() => {
    fetch('http://localhost:8080/api/jobs')
      .then(res => {
        if (!res.ok) {
          throw new Error('Failed to fetch jobs');
        }
        return res.json();
      })
      .then(data => {
        setJobs(data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Failed to fetch jobs', err);
        setLoading(false);
      });
  }, []);

  // =========================
  // Filtering & Sorting
  // =========================
  const filteredJobs = useMemo(() => {
    let filtered = [...jobs];

    // Filter by category
    if (category) {
      filtered = filtered.filter(job => job.category === category);
    }

    // Filter by search text
    if (search) {
      const searchLower = search.toLowerCase();
      filtered = filtered.filter(job =>
        job.title?.toLowerCase().includes(searchLower) ||
        job.company?.toLowerCase().includes(searchLower) ||
        job.description?.toLowerCase().includes(searchLower)
      );
    }

    // Filter by job type (if backend supports it)
    if (type) {
      filtered = filtered.filter(job => job.type === type);
    }

    // Sorting
    switch (sort) {
      case 'newest':
        filtered.sort(
          (a, b) =>
            new Date(b.postedAt || 0).getTime() -
            new Date(a.postedAt || 0).getTime()
        );
        break;

      case 'salary-high':
        filtered.sort(
          (a, b) => (b.salary?.max || 0) - (a.salary?.max || 0)
        );
        break;

      case 'salary-low':
        filtered.sort(
          (a, b) => (a.salary?.min || 0) - (b.salary?.min || 0)
        );
        break;
    }

    return filtered;
  }, [jobs, category, search, type, sort]);

  // =========================
  // Helpers
  // =========================
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

  // =========================
  // UI
  // =========================
  return (
    <Layout>
      <SEOHead
        title="Browse Online Jobs"
        description="Find the best online jobs in data entry, typing, captcha solving, form filling, and more."
        canonical="/jobs"
        keywords="online jobs, remote jobs, work from home"
      />

      {/* Header */}
      <section className="bg-muted/30 py-12">
        <div className="container mx-auto px-4">
          <nav className="flex items-center gap-2 text-sm text-muted-foreground mb-6">
            <Link to="/" className="hover:text-foreground">Home</Link>
            <span>/</span>
            <span className="text-foreground">Jobs</span>
          </nav>

          <h1 className="text-3xl md:text-4xl font-bold mb-4">
            Browse All Online Jobs
          </h1>

          <p className="text-muted-foreground mb-8">
            {filteredJobs.length} jobs available
          </p>

          <SearchBar />
        </div>
      </section>

      {/* Content */}
      <section className="py-12">
        <div className="container mx-auto px-4">
          <div className="flex flex-col lg:flex-row gap-8">

            {/* Sidebar */}
            <aside className={`lg:w-72 ${showFilters ? 'block' : 'hidden lg:block'}`}>
              <div className="bg-card p-6 border rounded-xl sticky top-24">
                <div className="flex justify-between mb-6">
                  <h2 className="font-semibold">Filters</h2>
                  {activeFiltersCount > 0 && (
                    <Button size="sm" variant="ghost" onClick={clearFilters}>
                      Clear all
                    </Button>
                  )}
                </div>

                {/* Category */}
                <div className="mb-6">
                  <label className="text-sm font-medium mb-2 block">Category</label>
                  <Select
                    value={category || 'all'}
                    onValueChange={val => updateFilter('category', val === 'all' ? '' : val)}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All</SelectItem>
                      {JOB_CATEGORIES.map(cat => (
                        <SelectItem key={cat.value} value={cat.value}>
                          {cat.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>
            </aside>

            {/* Jobs */}
            <div className="flex-1">

              {loading && (
                <div className="text-center py-16 text-muted-foreground">
                  Loading jobs...
                </div>
              )}

              {!loading && filteredJobs.length > 0 && (
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
              )}

              {!loading && filteredJobs.length === 0 && (
                <div className="text-center py-16">
                  <h3 className="text-xl font-semibold mb-2">No jobs found</h3>
                  <Button onClick={clearFilters}>Clear Filters</Button>
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
